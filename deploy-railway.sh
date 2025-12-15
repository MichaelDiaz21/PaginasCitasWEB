#!/bin/bash
# Script para preparar el proyecto para despliegue en Railway
# Uso: bash deploy-railway.sh

echo "🚀 Preparando proyecto para Railway..."

# Verificar Maven
if ! command -v mvnw &> /dev/null; then
    echo "❌ Maven no encontrado. Asegúrate de estar en el directorio del proyecto."
    exit 1
fi

# Limpiar compilaciones anteriores
echo "🧹 Limpiando compilaciones anteriores..."
./mvnw clean

# Compilar el proyecto
echo "🔨 Compilando proyecto..."
./mvnw package -DskipTests

if [ $? -eq 0 ]; then
    echo "✅ Compilación exitosa!"
    echo ""
    echo "📋 Próximos pasos:"
    echo ""
    echo "1. Sube los cambios a GitHub:"
    echo "   git add ."
    echo "   git commit -m 'Configurar para Railway'"
    echo "   git push origin main"
    echo ""
    echo "2. Ve a railway.app e inicia sesión"
    echo ""
    echo "3. Crea un nuevo proyecto y selecciona tu repositorio"
    echo ""
    echo "4. Añade las variables de entorno en el Dashboard:"
    echo "   - DATABASE_URL"
    echo "   - DATABASE_USER"
    echo "   - DATABASE_PASSWORD"
    echo ""
    echo "   Ver: ENVIRONMENT_VARIABLES.md para valores exactos"
    echo ""
    echo "5. Railway desplegará automáticamente"
    echo ""
else
    echo "❌ La compilación falló. Revisa los errores arriba."
    exit 1
fi
