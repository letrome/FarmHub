<?php

class AnimalModel
{
    public string|null $id;
    public string $name;
    public string $birth_date;
    public string $specie;
    public string $farmer;
    public string $status;
    public string $picture;

    public function __construct($name, $birth_date, $specie, $farmer, $status, $picture, $id = null)
    {
        $this->id = $id;
        $this->name = $name;
        $this->birth_date = $birth_date;
        $this->specie = $specie;
        $this->farmer = $farmer;
        $this->status = $status;
        $this->picture = $picture;
    }
}