const repository = require('./repository');

function getAllSpecies() {
  return repository.getAllSpecies();
}

function getSpecieById(id) {
  return repository.getSpecieById(id);
}

function createSpecie(specie) {
  return repository.createSpecie(specie);
}

function updateSpecie(id, updatedSpecie) {
  return repository.updateSpecie(id, updatedSpecie);
}

function patchSpecie(id, updatedSpecie) {
  return repository.patchSpecie(id, updatedSpecie);
}

function deleteSpecie(id) {
  return repository.deleteSpecie(id);
}

module.exports = {
  getAllSpecies,
  getSpecieById,
  createSpecie,
  updateSpecie,
  patchSpecie,
  deleteSpecie
};