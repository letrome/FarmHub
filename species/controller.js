const service = require('./service');

function getAllSpecies(req, res) {
  const species = service.getAllSpecies();
  res.json(species);
}

function getSpecieById(req, res) {
  const id = req.params.id;
  const species = service.getSpecieById(id);
  if (species) {
    res.json(species);
  } else {
    res.status(404).json({ error: 'Specie not found' });
  }
}

function createSpecie(req, res) {
  const specie = req.body;
  const newSpecie = service.createSpecie(specie);
  res.status(201).json(newSpecie);
}

function updateSpecie(req, res) {
  const id = req.params.id;
  const updatedSpecie = req.body;
  const specie = service.updateSpecie(id, updatedSpecie);
  if (specie) {
    res.json(specie);
  } else {
    res.status(404).json({ error: 'Specie not found' });
  }
}

function patchSpecie(req, res) {
  const id = req.params.id;
  const updatedFields = req.body;
  const specie = service.patchSpecie(id, updatedFields);
  if (specie) {
    res.json(specie);
  } else {
    res.status(404).json({ error: 'Specie not found' });
  }
}

function deleteSpecie(req, res) {
  const id = req.params.id;
  const deletedSpecie = service.deleteSpecie(id);
  if (deletedSpecie) {
    res.json(deletedSpecie);
  } else {
    res.status(404).json({ error: 'Specie not found' });
  }
}

function setupRoutes(app) {
  app.get('/species', getAllSpecies);
  app.get('/species/:id', getSpecieById);
  app.post('/species', createSpecie);
  app.put('/species/:id', updateSpecie);
  app.patch('/species/:id', patchSpecie);
  app.delete('/species/:id', deleteSpecie);
}

module.exports = {
  setupRoutes,
};