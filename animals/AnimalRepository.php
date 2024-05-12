<?php

class AnimalRepository
{
    private array $animals;
    private int $lastAssignedId;
    private string $dbFile;

    public function __construct()
    {
        $this->dbFile = '../resources/db.txt';
        $this->loadData();
    }

    private function loadData(): void
    {
        if (file_exists($this->dbFile) && filesize($this->dbFile) > 0) {
            $data = file_get_contents($this->dbFile);
            $data = unserialize($data);
            $this->animals = $data['animals'];
            $this->lastAssignedId = $data['lastAssignedId'];
            //$this->animals = unserialize($data);
            //$this->lastAssignedId = count($this->animals);
        } else {
            $this->animals = $this->getDefaultData();
            $this->lastAssignedId = count($this->animals);
            $this->saveData();
        }
    }

    private function saveData(): void
    {
        $data = [
            'animals' => $this->animals,
            'lastAssignedId' => $this->lastAssignedId
        ];
        //$data = serialize($this->animals);
        $data = serialize($data);
        file_put_contents($this->dbFile, $data);
    }

    public function getAllAnimals(): array
    {
        return $this->animals;
    }

    public function getAnimalById($id): AnimalModel|null
    {
        foreach ($this->animals as $animal) {
            if ($animal->id === $id) {
                return $animal;
            }
        }
        return null;
    }

    public function createAnimal(AnimalModel $animal): AnimalModel
    {
        $animal->id = "animal" . ($this->lastAssignedId + 1);
        $this->animals[] = $animal;

        $this->lastAssignedId = $this->lastAssignedId + 1;
        $this->saveData();

        return $animal;
    }

    public function updateAnimal($id, $updatedAnimal): AnimalModel|null
    {
        foreach ($this->animals as &$animal) {
            if ($animal->id === $id) {
                $updatedAnimal->id = $id;
                $animal = $updatedAnimal;
                $this->saveData();
                return $animal;
            }
        }
        return null;
    }

    public function patchAnimal($id, $updatedFields): AnimalModel | null
    {
        foreach ($this->animals as &$animal) {
            if ($animal->id === $id) {
                foreach ($updatedFields as $field => $value) {
                    if (property_exists($animal, $field)) {
                        $animal->$field = $value;
                    }
                }
                $this->saveData();
                return $animal;
            }
        }
        return null;
    }


    public function deleteAnimal($id): AnimalModel | null
    {
        foreach ($this->animals as $key => $animal) {
            if ($animal->id === $id) {
                unset($this->animals[$key]);
                $this->saveData();
                return $animal;
            }
        }
        return null;
    }

    public function reset(): void
    {
        $this->animals = $this->getDefaultData();
        $this->lastAssignedId = count($this->animals);
        $this->saveData();
    }

    private function getDefaultData(): array
    {
        return [
            new AnimalModel(name: 'Marguerite', birthDate: '1959-12-16', specie: 'specie1', owner: 'owner1', status: 'present', picture: 'picture1', id: 'animal1'),
            new AnimalModel(name: 'Clarabelle', birthDate: '1930-04-11', specie: 'specie1', owner: 'owner1', status: 'present', picture: 'picture2', id: 'animal2'),
            new AnimalModel(name: 'Bessie', birthDate: '1975-08-01', specie: 'specie1', owner: 'owner2', status: 'gone', picture: 'picture3', id: 'animal3'),
            new AnimalModel(name: 'La vache qui rit', birthDate: '1921-04-16', specie: 'specie1', owner: 'owner2', status: 'present', picture: 'picture4', id: 'animal4'),

            new AnimalModel(name: 'Chicken Run', birthDate: '2000-06-23', specie: 'specie2', owner: 'owner3', status: 'present', picture: 'picture5', id: 'animal5'),
            new AnimalModel(name: 'Chicken Little', birthDate: '2005-10-30', specie: 'specie2', owner: 'owner3', status: 'present', picture: 'picture6', id: 'animal6'),
            new AnimalModel(name: 'Footix', birthDate: '1996-11-27', specie: 'specie2', owner: 'owner4', status: 'present', picture: 'picture7', id: 'animal7'),
            new AnimalModel(name: 'Monique', birthDate: '2014-03-01', specie: 'specie2', owner: 'owner4', status: 'gone', picture: 'picture8', id: 'animal8'),

            new AnimalModel(name: 'Dolly', birthDate: '1996-07-05', specie: 'specie3', owner: 'owner1', status: 'gone', picture: 'picture9', id: 'animal9'),
            new AnimalModel(name: 'Shaun', birthDate: '2007-03-05', specie: 'specie3', owner: 'owner1', status: 'present', picture: 'picture10', id: 'animal10'),
            new AnimalModel(name: 'Shrek', birthDate: '1996-11-27', specie: 'specie3', owner: 'owner2', status: 'gone', picture: 'picture11', id: 'animal11'),
            new AnimalModel(name: 'Wooloo', birthDate: '2019-11-15', specie: 'specie3', owner: 'owner2', status: 'present', picture: 'picture12', id: 'animal12'),

            new AnimalModel(name: 'Tornado', birthDate: '1957-10-10', specie: 'specie4', owner: 'owner3', status: 'present', picture: 'picture13', id: 'animal13'),
            new AnimalModel(name: 'Jolly Jumper', birthDate: '1946-12-07', specie: 'specie4', owner: 'owner3', status: 'present', picture: 'picture14', id: 'animal14'),
            new AnimalModel(name: 'Epona', birthDate: '1998-11-21', specie: 'specie4', owner: 'owner4', status: 'present', picture: 'picture15', id: 'animal15'),
            new AnimalModel(name: 'My little Pony', birthDate: '1981-07-13', specie: 'specie4', owner: 'owner4', status: 'present', picture: 'picture16', id: 'animal16'),

            new AnimalModel(name: 'Babe', birthDate: '1996-02-21', specie: 'specie5', owner: 'owner1', status: 'present', picture: 'picture17', id: 'animal17'),
            new AnimalModel(name: 'Spider-Ham', birthDate: '1983-11-01', specie: 'specie5', owner: 'owner1', status: 'present', picture: 'picture18', id: 'animal18'),
            new AnimalModel(name: 'Naf-Naf', birthDate: '1890-01-01', specie: 'specie5', owner: 'owner2', status: 'present', picture: 'picture19', id: 'animal19'),
            new AnimalModel(name: 'Peppa pig', birthDate: '2004-05-31', specie: 'specie5', owner: 'owner2', status: 'present', picture: 'picture20', id: 'animal20'),

            new AnimalModel(name: 'Gédéon', birthDate: '1923-01-01', specie: 'specie6', owner: 'owner3', status: 'present', picture: 'picture21', id: 'animal21'),
            new AnimalModel(name: 'Saturnin', birthDate: '1965-11-18', specie: 'specie6', owner: 'owner3', status: 'present', picture: 'picture22', id: 'animal22'),
            new AnimalModel(name: 'Donald', birthDate: '1934-09-09', specie: 'specie6', owner: 'owner4', status: 'present', picture: 'picture23', id: 'animal23'),
            new AnimalModel(name: 'Howard', birthDate: '1973-12-19', specie: 'specie6', owner: 'owner4', status: 'present', picture: 'picture24', id: 'animal24'),
        ];
    }
}