const repository = require('./repository');

async function getAllSpecies() {
  return await repository.getAllSpecies();
}

async function getSpecieById(id) {
  return await repository.getSpecieById(id);
}

async function createSpecie(specie) {
  return await repository.createSpecie(specie);
}

async function updateSpecie(id, updatedSpecie) {
  return await repository.updateSpecie(id, updatedSpecie);
}

async function patchSpecie(id, updatedSpecie) {
  return await repository.patchSpecie(id, updatedSpecie);
}

async function deleteSpecie(id) {
  return await repository.deleteSpecie(id);
}

async function reset() {
  return await repository.reset();
}

module.exports = {
  getAllSpecies,
  getSpecieById,
  createSpecie,
  updateSpecie,
  patchSpecie,
  deleteSpecie,
  reset
};