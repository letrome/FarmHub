import json
from typing import Dict, List

from fastapi import APIRouter, Request, Response

import service

router = APIRouter()


@router.get("/farmers")
def get_all_farmers() -> List[Dict]:
    return [farmer.model_dump() for farmer in service.get_all_farmers()]


@router.get("/farmers/{farmer_id}")
def get_farmer_by_id(farmer_id: str, response: Response):
    farmer = service.get_farmer_by_id(farmer_id)

    if farmer is None:
        response.status_code = 404
        response.body = json.dumps({"error": "Farmer not found"}).encode("utf-8")
        return response

    return farmer.model_dump()


@router.post("/farmers")
async def create_farmer(request: Request, response: Response):
    body = await request.json()

    farmer = service.create_farmer(body)

    if farmer is None:
        response.status_code = 404
        response.body = json.dumps({"error": "Farmer not found"}).encode("utf-8")
        return response

    return farmer.model_dump()


@router.put("/farmers/{farmer_id}")
async def update_farmer(farmer_id: str, request: Request, response: Response):
    body = await request.json()
    farmer = service.update_farmer(farmer_id, body)

    if farmer is None:
        response.status_code = 404
        response.body = json.dumps({"error": "Farmer not found"}).encode("utf-8")
        return response

    return farmer.model_dump()


@router.patch("/farmers/{farmer_id}")
async def patch_farmer(farmer_id: str, request: Request, response: Response):
    body = await request.json()

    farmer = service.patch_farmer(farmer_id, body)

    if farmer is None:
        response.status_code = 404
        response.body = json.dumps({"error": "Farmer not found"}).encode("utf-8")
        return response

    return farmer.model_dump()


@router.delete("/farmers/{farmer_id}")
def delete_farmer(farmer_id: str, response: Response):
    farmer = service.delete_farmer(farmer_id)

    if farmer is None:
        response.status_code = 404
        response.body = json.dumps({"error": "Farmer not found"}).encode("utf-8")
        return response

    return farmer.model_dump()


@router.post("/reset")
def reset() -> Dict:
    service.reset()
    return {"applied": True}
