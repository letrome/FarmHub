
class Species {
  constructor(id, name, description, diet, natural_habitat, conservation_status) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.diet = diet;
    this.natural_habitat = natural_habitat;
    this.conservation_status = conservation_status;
  }
}

module.exports = Species;