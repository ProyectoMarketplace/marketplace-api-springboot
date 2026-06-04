#!/usr/bin/env bash
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT"

if ! command -v docker >/dev/null 2>&1; then
  echo "Docker no está instalado. Instálalo o levanta PostgreSQL manualmente en localhost:5432."
  exit 1
fi

if ! docker info >/dev/null 2>&1; then
  echo "Docker no está corriendo. Abre Docker Desktop e intenta de nuevo."
  exit 1
fi

docker compose up -d

echo "Esperando PostgreSQL..."
until docker compose exec -T postgres pg_isready -U postgres -d marketplace_db >/dev/null 2>&1; do
  sleep 1
done

echo "PostgreSQL listo en localhost:5432 (base: marketplace_db)"
