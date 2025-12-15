#!/bin/bash

API_URL="http://localhost:8080"

echo "=========================================="
echo "Pruebas del Compilador como Servicio"
echo "=========================================="
echo ""

echo "1. Health Check"
curl -s "${API_URL}/api/salud"
echo ""
echo ""

echo "2. Compilación simple: 2 + 3"
curl -s -X POST "${API_URL}/api/compilar" \
  -H "Content-Type: application/json" \
  -d '{"expresion": "2 + 3", "lenguaje": "ARITMETICA"}'
echo ""
echo ""

echo "3. Compilación con precedencia: 2 + 3 * 4"
curl -s -X POST "${API_URL}/api/compilar" \
  -H "Content-Type: application/json" \
  -d '{"expresion": "2 + 3 * 4", "lenguaje": "ARITMETICA"}'
echo ""
echo ""

echo "4. Compilación con paréntesis: (2 + 3) * 4"
curl -s -X POST "${API_URL}/api/compilar" \
  -H "Content-Type: application/json" \
  -d '{"expresion": "(2 + 3) * 4", "lenguaje": "ARITMETICA"}'
echo ""
echo ""

echo "5. Error sintáctico: 2 + + 3"
curl -s -X POST "${API_URL}/api/compilar" \
  -H "Content-Type: application/json" \
  -d '{"expresion": "2 + + 3", "lenguaje": "ARITMETICA"}'
echo ""
echo ""

echo "6. División por cero: 5 / 0"
curl -s -X POST "${API_URL}/api/compilar" \
  -H "Content-Type: application/json" \
  -d '{"expresion": "5 / 0", "lenguaje": "ARITMETICA"}'
echo ""
echo ""

echo "7. Métricas acumuladas:"
curl -s "${API_URL}/api/metricas"
echo ""
echo ""

echo "=========================================="
echo "Pruebas completadas"
echo "=========================================="
