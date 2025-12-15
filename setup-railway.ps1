#!/bin/bash
# Script de configuración para Windows (PowerShell)
# Para usarlo en PowerShell: .\setup-railway.ps1

Write-Host "================================" -ForegroundColor Cyan
Write-Host "Configuración para Railway" -ForegroundColor Cyan
Write-Host "PaginasCitasWEB" -ForegroundColor Cyan
Write-Host "================================" -ForegroundColor Cyan
Write-Host ""

# Verificar que estamos en el directorio correcto
if (-not (Test-Path "pom.xml")) {
    Write-Host "❌ Error: No se encuentra pom.xml" -ForegroundColor Red
    Write-Host "Por favor, ejecuta este script desde la raíz del proyecto" -ForegroundColor Red
    exit 1
}

Write-Host "✅ Se encontró pom.xml" -ForegroundColor Green
Write-Host ""

# Crear archivo .env local si no existe
if (-not (Test-Path ".env")) {
    Write-Host "📝 Creando archivo .env local..." -ForegroundColor Yellow
    Copy-Item ".env.example" ".env"
    Write-Host "✅ Archivo .env creado" -ForegroundColor Green
    Write-Host "   Edita .env con tus credenciales locales" -ForegroundColor Cyan
    Write-Host ""
}

# Compilar el proyecto
Write-Host "🔨 Compilando proyecto (esto puede tomar 1-2 minutos)..." -ForegroundColor Yellow
Write-Host ""

.\mvnw.cmd clean package -DskipTests

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "✅ Compilación exitosa!" -ForegroundColor Green
    Write-Host ""
    Write-Host "================================" -ForegroundColor Green
    Write-Host "PRÓXIMOS PASOS" -ForegroundColor Green
    Write-Host "================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "1️⃣  Sube los cambios a GitHub:" -ForegroundColor Cyan
    Write-Host "   git add ." -ForegroundColor White
    Write-Host "   git commit -m 'Configurar para Railway'" -ForegroundColor White
    Write-Host "   git push origin main" -ForegroundColor White
    Write-Host ""
    Write-Host "2️⃣  Ve a railway.app:" -ForegroundColor Cyan
    Write-Host "   https://railway.app" -ForegroundColor White
    Write-Host ""
    Write-Host "3️⃣  Crea un nuevo proyecto:" -ForegroundColor Cyan
    Write-Host "   - Database -> MySQL" -ForegroundColor White
    Write-Host "   - GitHub Repo -> Selecciona tu repositorio" -ForegroundColor White
    Write-Host ""
    Write-Host "4️⃣  Configura las variables de entorno:" -ForegroundColor Cyan
    Write-Host "   En Dashboard -> Variables:" -ForegroundColor White
    Write-Host "   DATABASE_URL=mysql://usuario:pass@host:puerto/db?useSSL=false&serverTimezone=UTC" -ForegroundColor White
    Write-Host "   DATABASE_USER=usuario" -ForegroundColor White
    Write-Host "   DATABASE_PASSWORD=contraseña" -ForegroundColor White
    Write-Host ""
    Write-Host "📚 Consulta estos archivos para más información:" -ForegroundColor Yellow
    Write-Host "   - QUICK_START.md (Inicio rápido)" -ForegroundColor White
    Write-Host "   - RAILWAY_DEPLOYMENT.md (Guía completa)" -ForegroundColor White
    Write-Host "   - ENVIRONMENT_VARIABLES.md (Variables disponibles)" -ForegroundColor White
    Write-Host ""
} else {
    Write-Host ""
    Write-Host "❌ La compilación falló" -ForegroundColor Red
    Write-Host "Revisa los errores arriba" -ForegroundColor Red
    exit 1
}
