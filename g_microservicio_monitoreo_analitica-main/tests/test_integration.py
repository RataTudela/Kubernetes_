import pytest

def test_integracion_ciclo_completo_kpi(client):
    """
    INTEGRACIÓN: Valida el ciclo HTTP completo de un KPI 
    (Crear -> Listar -> Obtener por ID -> Actualizar -> Eliminar).
    """
    # 1. POST - Crear un KPI
    payload = {"nombre": "Disponibilidad", "descripcion": "Uptime del servidor", "valor": 99.9}
    response = client.post("/api/analisis/kpi", json=payload)
    assert response.status_code == 200
    data = response.json()
    assert data["nombre"] == "Disponibilidad"
    kpi_id = data["id_kpi"]

    # 2. GET - Listar todos los KPIs y verificar que exista
    response = client.get("/api/analisis/kpi")
    assert response.status_code == 200
    assert len(response.json()) == 1

    # 3. GET - Obtener el KPI específico por ID
    response = client.get(f"/api/analisis/kpi/{kpi_id}")
    assert response.status_code == 200
    assert response.json()["nombre"] == "Disponibilidad"

    # 4. PUT - Actualizar el KPI (Acción de UPSERT integrada)
    update_payload = {"nombre": "Disponibilidad", "descripcion": "Uptime mejorado", "valor": 95.5}
    response = client.put(f"/api/analisis/kpi/{kpi_id}", json=update_payload)
    assert response.status_code == 200
    assert response.json()["valor"] == 95.5

    # 5. DELETE - Eliminar el KPI
    response = client.delete(f"/api/analisis/kpi/{kpi_id}")
    assert response.status_code == 200
    assert response.json()["message"] == "KPI eliminado correctamente"

    # 6. GET - Verificar que ya no exista (Debe dar 404)
    response = client.get(f"/api/analisis/kpi/{kpi_id}")
    assert response.status_code == 404


def test_integracion_recolectar_datos_analitica(client):
    """
    INTEGRACIÓN CRÍTICA: Simula el envío de proyectos desde el frontend.
    Verifica que se calculen las métricas, se disparen KPIs automáticos 
    y se registren los reportes correspondientes.
    """
    # Datos simulados del Frontend (1 proyecto sano, 1 proyecto crítico < 40%)
    payload_frontend = [
        {"id_proyecto": 10, "tareas_completadas": 8, "tareas_totales": 10},  # 80% Avance
        {"id_proyecto": 11, "tareas_completadas": 2, "tareas_totales": 10}   # 20% Avance (Crítico)
    ]

    # Ejecutamos la sincronización asíncrona a través del endpoint
    response = client.post("/api/analisis/reportes/periodico", json=payload_frontend)
    assert response.status_code == 200
    assert response.json()["status"] == "success"
    
    # El resumen calculado por tu lógica interna debe coincidir
    assert "2 proyectos" in response.json()["reporte_resumen"]
    assert "1 críticos" in response.json()["reporte_resumen"]

    # --- VERIFICACIÓN DE IMPACTO EN OTROS ENDPOINTS ---

    # 1. El endpoint de Alertas debe haber capturado al proyecto 11
    response_alertas = client.get("/api/analisis/alertas")
    assert response_alertas.status_code == 200
    alertas = response_alertas.json()["alertas_criticas"]
    assert len(alertas) == 1
    assert alertas[0]["id_proyecto"] == 11

    # 2. El endpoint de KPIs debe haber generado el KPI "Eficiencia General" de forma automática
    # El promedio de avances es (80 + 20) / 2 = 50.0%
    response_kpi = client.get("/api/analisis/kpi")
    kpis = response_kpi.json()
    assert len(kpis) == 1
    assert kpis[0]["nombre"] == "Eficiencia General"
    assert kpis[0]["valor"] == 50.0

    # 3. El Dashboard consolidado debe mostrar toda la información estructurada
    response_dash = client.get("/api/analisis/dashboard")
    assert response_dash.status_code == 200
    dash_data = response_dash.json()
    assert len(dash_data["kpis"]) == 1
    assert len(dash_data["metricas"]) == 2
    assert len(dash_data["reportes_recientes"]) == 1