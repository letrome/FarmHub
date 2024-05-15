package main

import (
	"encoding/json"
	"github.com/gorilla/mux"
	"net/http"
)

type PictureController struct {
	Service *PictureService
}

func NewPictureController(service *PictureService) *PictureController {
	return &PictureController{
		Service: service,
	}
}

func (c *PictureController) GetPictures(w http.ResponseWriter, _ *http.Request) {
	images, err := c.Service.GetAllPictures()
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	err = json.NewEncoder(w).Encode(images)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
}

func (c *PictureController) GetPicture(w http.ResponseWriter, r *http.Request) {
	params := mux.Vars(r)
	imageID := params["id"]

	image, err := c.Service.GetPictureByID(imageID)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	if image == nil {

		response := struct {
			Error string `json:"error"`
		}{
			Error: "Picture not found",
		}

		w.Header().Set("Content-Type", "application/json")
		w.WriteHeader(http.StatusNotFound)

		err = json.NewEncoder(w).Encode(response)

		if err != nil {
			http.Error(w, err.Error(), http.StatusInternalServerError)
			return
		}

		return
	}

	err = json.NewEncoder(w).Encode(image)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
}

func (c *PictureController) CreatePicture(w http.ResponseWriter, r *http.Request) {
	var image Picture
	err := json.NewDecoder(r.Body).Decode(&image)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	var outputPicture *Picture
	outputPicture, err = c.Service.CreatePicture(image)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	w.WriteHeader(http.StatusCreated)
	err = json.NewEncoder(w).Encode(outputPicture)

	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
}

func (c *PictureController) UpdatePicture(w http.ResponseWriter, r *http.Request) {
	params := mux.Vars(r)
	imageID := params["id"]

	var image Picture
	err := json.NewDecoder(r.Body).Decode(&image)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	var outputPicture *Picture

	outputPicture, err = c.Service.UpdatePicture(imageID, image)

	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	if outputPicture == nil {
		response := struct {
			Error string `json:"error"`
		}{
			Error: "Picture not found",
		}

		w.Header().Set("Content-Type", "application/json")
		w.WriteHeader(http.StatusNotFound)

		err = json.NewEncoder(w).Encode(response)

		if err != nil {
			http.Error(w, err.Error(), http.StatusInternalServerError)
			return
		}

		return
	}

	w.WriteHeader(http.StatusOK)

	err = json.NewEncoder(w).Encode(outputPicture)

	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
}

func (c *PictureController) PatchPicture(w http.ResponseWriter, r *http.Request) {
	params := mux.Vars(r)
	imageID := params["id"]

	var image Picture
	err := json.NewDecoder(r.Body).Decode(&image)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	var outputPicture *Picture

	outputPicture, err = c.Service.PatchPicture(imageID, image)

	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	if outputPicture == nil {
		response := struct {
			Error string `json:"error"`
		}{
			Error: "Picture not found",
		}

		w.Header().Set("Content-Type", "application/json")
		w.WriteHeader(http.StatusNotFound)

		err = json.NewEncoder(w).Encode(response)

		if err != nil {
			http.Error(w, err.Error(), http.StatusInternalServerError)
			return
		}

		return
	}

	w.WriteHeader(http.StatusOK)

	err = json.NewEncoder(w).Encode(outputPicture)

	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
}

func (c *PictureController) DeletePicture(w http.ResponseWriter, r *http.Request) {
	params := mux.Vars(r)
	imageID := params["id"]

	var image *Picture

	image, err := c.Service.DeletePicture(imageID)

	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	if image == nil {
		response := struct {
			Error string `json:"error"`
		}{
			Error: "Picture not found",
		}

		w.Header().Set("Content-Type", "application/json")
		w.WriteHeader(http.StatusNotFound)

		err = json.NewEncoder(w).Encode(response)

		if err != nil {
			http.Error(w, err.Error(), http.StatusInternalServerError)
			return
		}

		return
	}

	w.WriteHeader(http.StatusOK)

	err = json.NewEncoder(w).Encode(image)

	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
}

func (c *PictureController) Reset(w http.ResponseWriter, _ *http.Request) {

	err := c.Service.Reset()

	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	response := struct {
		Applied bool `json:"applied"`
	}{
		Applied: true,
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)

	err = json.NewEncoder(w).Encode(response)
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}
}
