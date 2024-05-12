<?php

class AnimalService
{
    private AnimalRepository $animalRepository;

    public function __construct($animalRepository)
    {
        $this->animalRepository = $animalRepository;
    }

    public function getAllAnimals()
    {
        return $this->animalRepository->getAllAnimals();
    }

    public function getAnimalById($id)
    {
        return $this->animalRepository->getAnimalById($id);
    }

    public function createAnimal($animal)
    {
        return $this->animalRepository->createAnimal($animal);
    }

    public function updateAnimal($id, $updatedAnimal)
    {
        return $this->animalRepository->updateAnimal($id, $updatedAnimal);
    }

    public function patchAnimal($id, $updatedFields)
    {
        return $this->animalRepository->patchAnimal($id, $updatedFields);
    }


    public function deleteAnimal($id)
    {
        return $this->animalRepository->deleteAnimal($id);
    }

    public function reset()
    {
        $this->animalRepository->reset();
    }
}