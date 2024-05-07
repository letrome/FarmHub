const express = require('express');
const bodyParser = require('body-parser');
const service = require('./service');

const app = express();

app.use(bodyParser.json());

app.get('/species', (req, res) => {
  const species = service.getAllSpecies();
  res.json(species);
});

app.get('/species/:id', (req, res) => {
  const id = req.params.id;
  const species = service.getSpecieById(id);
  if (species) {
    res.json(species);
  } else {
    res.status(404).json({ error: 'Specie not found' });
  }
});

app.post('/species', (req, res) => {
  const specie = req.body;
  const newSpecie = service.createSpecie(specie);
  res.status(201).json(newSpecie);
});

app.put('/species/:id', (req, res) => {
  const id = req.params.id;
  const updatedSpecie = req.body;
  const specie = service.updateSpecie(id, updatedSpecie);
  if (specie) {
    res.json(specie);
  } else {
    res.status(404).json({ error: 'Specie not found' });
  }
});

app.patch('/species/:id', (req, res) => {
  const id = req.params.id;
  const updatedFields = req.body;
  const specie = service.patchSpecie(id, updatedFields);
  if (specie) {
    res.json(specie);
  } else {
    res.status(404).json({ error: 'Specie not found' });
  }
});

app.delete('/species/:id', (req, res) => {
  const id = req.params.id;
  const deletedSpecie = service.deleteSpecie(id);
  if (deletedSpecie) {
    res.json(deletedSpecie);
  } else {
    res.status(404).json({ error: 'Specie not found' });
  }
});

const port = process.argv[2] || 3000;

app.listen(port, () => {
  console.log(`Server running on port ${port}`);
});