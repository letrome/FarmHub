<?php

class AnimalModel
{
    public $id;
    public $name;
    public $birthDate;
    public $specie;
    public $owner;
    public $status;
    public $picture;

    public function __construct($name, $birthDate, $specie, $owner, $status, $picture, $id = null)
    {
        $this->id = $id;
        $this->name = $name;
        $this->birthDate = $birthDate;
        $this->specie = $specie;
        $this->owner = $owner;
        $this->status = $status;
        $this->picture = $picture;
    }
}