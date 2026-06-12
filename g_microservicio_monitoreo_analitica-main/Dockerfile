# 1. Usar una imagen oficial de Python liviana
FROM python:3.11-slim

# 2. Configurar variables de entorno esenciales
# Evita que Python escriba archivos .pyc en el contenedor
ENV PYTHONDONTWRITEBYTECODE=1
# Evita que Python guarde en buffer los archivos de salida (logs en tiempo real)
ENV PYTHONUNBUFFERED=1
# Configura el PYTHONPATH para que 'app' sea reconocible desde la raíz
ENV PYTHONPATH=/code

# 3. Establecer el directorio de trabajo dentro del contenedor
WORKDIR /code

# 4. Copiar e instalar dependencias primero (aprovecha la caché de Docker)
COPY ./requirements.txt /code/requirements.txt
RUN pip install --no-cache-dir --upgrade -r /code/requirements.txt

# 5. Copiar el resto del código del proyecto
COPY ./app /code/app

# 6. Exponer el puerto en el que correrá FastAPI (por defecto suele ser 8000)
EXPOSE 8000

# 7. Comando para ejecutar la aplicación usando Uvicorn
CMD ["uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "8000"]

#comentnado para probar si soy colaborador