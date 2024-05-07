const Species = require('./model');

let speciesData = [
  new Species('specie1', 'Cow', 'The cow is a domesticated herbivorous mammal.', 'Herbivore', 'Grasslands', 'Least Concern'),
  new Species('specie2', 'Chicken', 'The chicken is a domesticated bird.', 'Omnivore', 'Farmland', 'Least Concern'),
  new Species('specie3', 'Sheep', 'The sheep is a domesticated herbivorous mammal.', 'Herbivore', 'Grasslands', 'Least Concern'),
  new Species('specie4', 'Horse', 'The horse is a large herbivorous mammal.', 'Herbivore', 'Grasslands', 'Least Concern'),
  new Species('specie5', 'Pig', 'The pig is a domesticated omnivorous mammal.', 'Omnivore', 'Farmland', 'Least Concern'),
  new Species('specie6', 'Duck', 'The duck is a waterfowl bird.', 'Omnivore', 'Wetlands', 'Least Concern')
];

let lastAssignedId = 6;

function getAllSpecies() {
  return speciesData;
}

function getSpecieById(id) {
  return speciesData.find(species => species.id === id);
}

function createSpecie(species) {
  const newId = "specie"+(lastAssignedId + 1).toString();
  const newSpecie = new Species(newId, species.name, species.description, species.diet, species.naturalHabitat, species.conservationStatus);
  speciesData.push(newSpecie);
  lastAssignedId++;
  return newSpecie;
}

function updateSpecie(id, updatedSpecie) {
  const index = speciesData.findIndex(species => species.id === id);
  if (index !== -1) {
    updatedSpecie.id = id;
    speciesData[index] = updatedSpecie;
    return updatedSpecie;
  }
  return null;
}

function patchSpecie(id, updatedFields) {
  const index = speciesData.findIndex(species => species.id === id);
  if (index !== -1) {
    const originalSpecies = speciesData[index];
    const updatedSpecies = {
      id: originalSpecies.id,
      name: updatedFields.name || originalSpecies.name,
      description: updatedFields.description || originalSpecies.description,
      diet: updatedFields.diet || originalSpecies.diet,
      naturalHabitat: updatedFields.naturalHabitat || originalSpecies.naturalHabitat,
      conservationStatus: updatedFields.conservationStatus || originalSpecies.conservationStatus
    };
    speciesData[index] = new Species(
      updatedSpecies.id,
      updatedSpecies.name,
      updatedSpecies.description,
      updatedSpecies.diet,
      updatedSpecies.naturalHabitat,
      updatedSpecies.conservationStatus
    );
    return speciesData[index];
  }
  return null;
}

function deleteSpecie(id) {
  const index = speciesData.findIndex(species => species.id === id);
  if (index !== -1) {
    const deletedSpecies = speciesData.splice(index, 1);
    return deletedSpecies[0];
  }
  return null;
}

module.exports = {
  getAllSpecies,
  getSpecieById,
  createSpecie,
  updateSpecie,
  patchSpecie,
  deleteSpecie
};