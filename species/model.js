
class Species {
  constructor(id, name, description, diet, naturalHabitat, conservationStatus) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.diet = diet;
    this.naturalHabitat = naturalHabitat;
    this.conservationStatus = conservationStatus;
  }
}

module.exports = Species;