#!/bin/bash

# Redis Cluster Management Script
# This script helps manage the Redis cluster with proper IP handling

# No project name needed - using container names directly

# Valid Redis nodes
VALID_NODES=("redis-7001" "redis-7002" "redis-7003" "redis-7004" "redis-7005" "redis-7006")
DEFAULT_NODE="redis-7001"
SELECTED_NODE="$DEFAULT_NODE"
REDIS_COMMAND=""

function show_help() {
    echo "Redis Cluster Management Script"
    echo ""
    echo "Usage: $0 [command] [options]"
    echo ""
    echo "Commands:"
    echo "  start       Start Redis cluster or specific node (default: all nodes)"
    echo "  stop        Stop Redis cluster or specific node (default: all nodes)"
    echo "  restart     Restart Redis cluster or specific node (default: all nodes)"
    echo "  status      Show cluster status (default: from redis-7001)"
    echo "  nodes       Show cluster nodes (default: from redis-7001)"
    echo "  reset       Reset cluster state (destructive - removes all data)"
    echo "  logs        Show logs from all containers or specific node (default: all)"
    echo "  cli         Connect to redis-cli (default: redis-7001)"
    echo "  help        Show this help message"
    echo ""
    echo "Options:"
    echo "  -n, --node NODE      Specify Redis node for operations"
    echo "                       Valid nodes: redis-7001, redis-7002, redis-7003, redis-7004, redis-7005, redis-7006"
    echo "                       Default: all nodes (for start/stop/restart/logs), redis-7001 (for cli/status/nodes)"
    echo "  -c, --command CMD    Execute Redis command directly (cli only)"
    echo "                       Command will be executed and script will exit"
    echo ""
    echo "Examples:"
    echo "  $0 start                          # Start entire cluster"
    echo "  $0 start -n redis-7002           # Start only redis-7002"
    echo "  $0 stop --node redis-7003        # Stop only redis-7003"
    echo "  $0 logs -n redis-7001            # Show logs for redis-7001 only"
    echo "  $0 restart                       # Restart entire cluster"
    echo "  $0 cli                           # Interactive CLI on redis-7001"
    echo "  $0 cli -n redis-7002             # Interactive CLI on redis-7002"
    echo "  $0 cli -c 'SET mykey myvalue'    # Execute command on redis-7001"
    echo "  $0 cli -n redis-7003 -c 'GET mykey'  # Execute command on redis-7003"
    echo "  $0 cli --command 'CLUSTER NODES' # Execute cluster command"
    echo "  $0 status --node redis-7003      # Show status from redis-7003"
}

function start_cluster() {
    local node="$1"

    if [[ -n "$node" ]]; then
        echo "🚀 Starting Redis node: $node..."
        docker compose up -d "$node"

        echo "⏳ Waiting for node to be ready..."
        sleep 3

        local port=$(get_port_from_node "$node")
        if docker exec "$node" redis-cli -p "$port" ping 2>/dev/null | grep -q "PONG"; then
            echo "✅ Redis node $node started successfully!"
        else
            echo "❌ Node $node may have failed to start properly. Check logs:"
            docker logs "$node"
        fi
    else
        echo "🚀 Starting Redis cluster..."
        docker compose up -d

        echo "⏳ Waiting for cluster initialization..."
        sleep 8

        # Check if cluster was initialized successfully
        if docker logs redis-cluster-init 2>/dev/null | grep -q "Cluster initialized successfully\\|Cluster already properly configured"; then
            echo "✅ Redis cluster started successfully!"
            show_status
        else
            echo "❌ Cluster initialization may have failed. Check logs:"
            docker logs redis-cluster-init
        fi
    fi
}

function stop_cluster() {
    local node="$1"

    if [[ -n "$node" ]]; then
        echo "🛑 Stopping Redis node: $node..."
        docker compose stop "$node"
        echo "✅ Redis node $node stopped."
    else
        echo "🛑 Stopping Redis cluster..."
        docker compose down
        echo "✅ Redis cluster stopped."
    fi
}

function restart_cluster() {
    local node="$1"

    if [[ -n "$node" ]]; then
        echo "🔄 Restarting Redis node: $node..."
        stop_cluster "$node"
        sleep 2
        start_cluster "$node"
    else
        echo "🔄 Restarting Redis cluster..."
        stop_cluster
        sleep 2
        start_cluster
    fi
}

function show_status() {
    local node="${1:-$DEFAULT_NODE}"
    local port=$(get_port_from_node "$node")

    echo "📊 Redis Cluster Status (via $node):"
    echo "========================"

    if docker ps | grep -q "redis-700"; then
        echo "Cluster Info:"
        docker exec "$node" redis-cli -p "$port" cluster info 2>/dev/null || echo "Cannot connect to cluster"
        echo ""
        echo "Cluster Nodes:"
        docker exec "$node" redis-cli -p "$port" cluster nodes 2>/dev/null || echo "Cannot retrieve cluster nodes"
    else
        echo "❌ Redis cluster is not running. Use '$0 start' to start it."
    fi
}

function show_nodes() {
    local node="${1:-$DEFAULT_NODE}"
    local port=$(get_port_from_node "$node")

    echo "🔗 Cluster Nodes (via $node):"
    echo "=================="
    docker exec "$node" redis-cli -p "$port" cluster nodes 2>/dev/null || echo "Cannot retrieve cluster nodes"
}

function reset_cluster() {
    echo "⚠️  WARNING: This will reset the entire cluster and remove all data!"
    read -p "Are you sure you want to continue? (yes/no): " confirm

    if [[ $confirm == "yes" ]]; then
        echo "🗑️  Resetting cluster..."
        stop_cluster
        echo "Removing volumes..."
        docker compose down -v
        echo "✅ Cluster reset complete. Use '$0 start' to start fresh."
    else
        echo "❌ Reset cancelled."
    fi
}

function get_port_from_node() {
    local node="$1"
    case "$node" in
        "redis-7001") echo "7001" ;;
        "redis-7002") echo "7002" ;;
        "redis-7003") echo "7003" ;;
        "redis-7004") echo "7004" ;;
        "redis-7005") echo "7005" ;;
        "redis-7006") echo "7006" ;;
        *) echo "7001" ;;  # fallback
    esac
}

function validate_node() {
    local node="$1"
    for valid_node in "${VALID_NODES[@]}"; do
        if [[ "$node" == "$valid_node" ]]; then
            return 0
        fi
    done
    return 1
}

function parse_arguments() {
    while [[ $# -gt 0 ]]; do
        case $1 in
            -n|--node)
                if [[ -n "$2" ]] && [[ ! "$2" =~ ^- ]]; then
                    if validate_node "$2"; then
                        SELECTED_NODE="$2"
                        shift 2
                    else
                        echo "❌ Error: Invalid node '$2'"
                        echo "Valid nodes: ${VALID_NODES[*]}"
                        exit 1
                    fi
                else
                    echo "❌ Error: --node requires a value"
                    echo "Valid nodes: ${VALID_NODES[*]}"
                    exit 1
                fi
                ;;
            -c|--command)
                if [[ -n "$2" ]] && [[ ! "$2" =~ ^- ]]; then
                    REDIS_COMMAND="$2"
                    shift 2
                else
                    echo "❌ Error: --command requires a value"
                    echo "Example: --command 'SET mykey myvalue'"
                    exit 1
                fi
                ;;
            *)
                shift
                ;;
        esac
    done
}

function get_node_for_cluster_ops() {
    # For cluster operations (start/stop/restart/logs), return the node if specified, otherwise empty for "all"
    if [[ "$SELECTED_NODE" != "$DEFAULT_NODE" ]]; then
        echo "$SELECTED_NODE"
    else
        echo ""
    fi
}

function show_logs() {
    local node="$1"

    if [[ -n "$node" ]]; then
        echo "📋 Showing logs for Redis node: $node..."
        docker compose logs -f "$node"
    else
        echo "📋 Showing Redis cluster logs..."
        docker compose logs -f
    fi
}

function connect_cli() {
    local node="${1:-$DEFAULT_NODE}"
    local command="$2"
    local port=$(get_port_from_node "$node")
    
    if [[ -n "$command" ]]; then
        echo "🚀 Executing Redis command on $node: $command"
        docker exec "$node" redis-cli -p "$port" -c $command
    else
        echo "🔌 Connecting to Redis CLI on $node..."
        docker exec -it "$node" redis-cli -p "$port" -c
    fi
}

# Parse arguments
COMMAND="${1:-help}"
shift # Remove command from arguments
parse_arguments "$@"

# Main script logic
case "$COMMAND" in
    start)
        start_cluster "$(get_node_for_cluster_ops)"
        ;;
    stop)
        stop_cluster "$(get_node_for_cluster_ops)"
        ;;
    restart)
        restart_cluster "$(get_node_for_cluster_ops)"
        ;;
    status)
        show_status "$SELECTED_NODE"
        ;;
    nodes)
        show_nodes "$SELECTED_NODE"
        ;;
    reset)
        reset_cluster
        ;;
    logs)
        show_logs "$(get_node_for_cluster_ops)"
        ;;
    cli)
        connect_cli "$SELECTED_NODE" "$REDIS_COMMAND"
        ;;
    help|*)
        show_help
        ;;
esac
