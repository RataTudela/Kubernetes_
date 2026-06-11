import os
import pytest
from fastapi.testclient import TestClient
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker

from app.database import Base, get_db
from app.main import app

# 1. Configuración de la base de datos de prueba (Archivo SQLite local temporal)
# Usamos un archivo en lugar de ':memory:' para que el TestClient y los tests compartan las tablas.
SQLALCHEMY_DATABASE_URL = "sqlite:///./test_db.db"

engine = create_engine(
    SQLALCHEMY_DATABASE_URL, 
    connect_args={"check_same_thread": False}
)
TestingSessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

@pytest.fixture(scope="session", autouse=True)
def setup_test_database():
    """
    Fixture de sesión: Se ejecuta UNA sola vez al inicio de todos los tests.
    Crea el archivo de la base de datos y la estructura de tablas.
    Al finalizar todas las pruebas, limpia el archivo residual asegurando cerrar conexiones.
    """
    # Crear todas las tablas mapeadas en los modelos
    Base.metadata.create_all(bind=engine)
    
    yield  # Aquí es donde pytest ejecuta todos tus archivos de prueba
    
    # Al terminar la suite de pruebas, eliminamos las tablas
    Base.metadata.drop_all(bind=engine)
    
    # ¡CORRECCIÓN AQUÍ!: Cierra y libera todas las conexiones activas al archivo SQLite
    engine.dispose()
    
    # Ahora que Windows sabe que nadie usa el archivo, lo borramos de forma segura
    if os.path.exists("test_db.db"):
        os.remove("test_db.db")

@pytest.fixture(scope="function")
def db_session():
    """Crea un ciclo de vida limpio para la sesión de base de datos en cada prueba."""
    session = TestingSessionLocal()
    try:
        yield session
    finally:
        session.close()

@pytest.fixture(scope="function")
def client(db_session):
    """
    Inyecta la base de datos de prueba dentro de FastAPI 
    y retorna un cliente HTTP para interactuar con los endpoints.
    """
    def _get_test_db():
        try:
            yield db_session
        finally:
            pass

    # Reemplazamos la dependencia real de Neon por nuestra sesión de pruebas SQLite
    app.dependency_overrides[get_db] = _get_test_db
    
    with TestClient(app) as test_client:
        yield test_client
    
    # Limpiamos los overrides al terminar la prueba
    app.dependency_overrides.clear()