from typing import List, Optional, Dict

from model import FarmerModel
from repository import FarmerRepository

farmer_repository = FarmerRepository()


def get_all_farmers() -> List[FarmerModel]:
    return farmer_repository.get_all_farmers()


def get_farmer_by_id(farmer_id: str) -> Optional[FarmerModel]:
    return farmer_repository.get_farmer_by_id(farmer_id)


def create_farmer(farmer: Dict) -> FarmerModel:
    return farmer_repository.create_farmer(farmer)


def update_farmer(farmer_id: str, farmer: Dict) -> Optional[FarmerModel]:
    return farmer_repository.update_farmer(farmer_id, farmer)


def patch_farmer(farmer_id: str, farmer: Dict) -> Optional[FarmerModel]:
    return farmer_repository.patch_farmer(farmer_id, farmer)


def delete_farmer(farmer_id: str) -> Optional[FarmerModel]:
    return farmer_repository.delete_farmer(farmer_id)


def reset() -> None:
    return farmer_repository.reset()
