from typing import List

from pydantic import BaseModel


class FarmerModel(BaseModel):
    id: str
    first_name: str
    last_name: str
    birth_date: str
    specialties: List[str]
    picture: str
    status: str
