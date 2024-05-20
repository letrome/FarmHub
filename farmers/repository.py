import json
from json import JSONDecodeError
from typing import List, Optional, Dict

from model import FarmerModel

FILE_PATH = "resources/db.txt"


class FarmerRepository:
    def __init__(self):
        self.last_assigned_id: int = -1
        self.farmers: List = []
        self.load_data()

    @staticmethod
    def get_default_data():
        return [
            FarmerModel(id="farmer1", first_name="Alice", last_name="Cooper", birth_date="1948-02-04",
                        specialties=["Cow", "Sheep", "Pig"], picture="picture25", status="gone"),
            FarmerModel(id="farmer2", first_name="Bob", last_name="Sinclar", birth_date="1969-05-10",
                        specialties=["Cow", "Sheep", "Pig"], picture="picture26", status="present"),
            FarmerModel(id="farmer3", first_name="Eve", last_name="Angeli", birth_date="1980-08-25",
                        specialties=["Chicken", "Horse", "Duck"], picture="picture27",
                        status="gone"),
            FarmerModel(id="farmer4", first_name="Mallory", last_name="Gabsi", birth_date="1996-12-13",
                        specialties=["Chicken", "Horse", "Duck"], picture="picture28",
                        status="present"),
        ], 4

    def load_data(self):
        with open(FILE_PATH, "r") as file:
            try:
                data = json.load(file)
            except JSONDecodeError:
                data = {}

        if len(data) > 0:
            self.farmers = [FarmerModel(**item) for item in data['farmers']]
            self.last_assigned_id = data['last_assigned_id']
        else:
            self.farmers, self.last_assigned_id = self.get_default_data()
            self.save_data()

    def save_data(self):
        with open(FILE_PATH, "w") as file:
            json.dump({
                'last_assigned_id': self.last_assigned_id,
                'farmers': [farmer.dict() for farmer in self.farmers]
            }, file, indent=2)

    def get_all_farmers(self) -> List[FarmerModel]:
        return self.farmers

    def get_farmer_by_id(self, farmer_id: str) -> Optional[FarmerModel]:
        for farmer in self.farmers:
            if farmer.id == farmer_id:
                return farmer
        return None

    def create_farmer(self, data: Dict) -> FarmerModel:
        farmer_model = FarmerModel(id="farmer" + str(self.last_assigned_id + 1), first_name=data["first_name"],
                                   last_name=data["last_name"], birth_date=data["birth_date"],
                                   specialties=data["specialties"], picture=data["picture"],
                                   status=data["status"])
        self.farmers.append(farmer_model)

        self.last_assigned_id += 1
        self.save_data()

        return farmer_model

    def update_farmer(self, farmer_id: str, data: Dict) -> Optional[FarmerModel]:
        for farmer in self.farmers:
            if farmer.id == farmer_id:
                new_farmer = FarmerModel(id=farmer.id, first_name=data["first_name"],
                                         last_name=data["last_name"], birth_date=data["birth_date"],
                                         specialties=data["specialties"], picture=data["picture"],
                                         status=data["status"])
                self.farmers.remove(farmer)
                self.farmers.append(new_farmer)
                self.save_data()
                return new_farmer

        return None

    def patch_farmer(self, farmer_id: str, data: Dict):
        for farmer in self.farmers:
            if farmer.id == farmer_id:
                new_farmer = FarmerModel(
                    id=farmer_id,
                    first_name=data['first_name'] if 'first_name' in data.keys() else farmer.first_name,
                    last_name=data['last_name'] if 'last_name' in data.keys() else farmer.last_name,
                    status=data['status'] if 'status' in data.keys() else farmer.status,
                    picture=data['picture'] if 'picture' in data.keys() else farmer.picture,
                    birth_date=data['birth_date'] if 'birth_date' in data.keys() else farmer.birth_date,
                    specialties=data['specialties'] if 'specialties' in data.keys() else farmer.specialties
                )

                self.farmers.remove(farmer)
                self.farmers.append(new_farmer)

                self.save_data()
                return new_farmer

        return None

    def delete_farmer(self, farmer_id: str) -> Optional[FarmerModel]:
        for farmer in self.farmers:
            if farmer.id == farmer_id:
                self.farmers.remove(farmer)
                self.save_data()

                return farmer

        return None

    def reset(self) -> None:
        self.farmers, self.last_assigned_id = self.get_default_data()
        self.save_data()
