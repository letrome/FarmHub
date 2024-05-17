<?php

class AnimalModel
{
    public string|null $id;
    public string $name;
    public string $birthDate;
    public string $specie;
    public string $farmer;
    public string $status;
    public string $picture;

    public function __construct($name, $birthDate, $specie, $farmer, $status, $picture, $id = null)
    {
        $this->id = $id;
        $this->name = $name;
        $this->birthDate = $birthDate;
        $this->specie = $specie;
        $this->farmer = $farmer;
        $this->status = $status;
        $this->picture = $picture;
    }
}