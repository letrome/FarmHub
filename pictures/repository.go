package main

import (
	"encoding/json"
	"os"
	"strconv"
)

type PictureRepository struct {
	FilePath       string
	LastAssignedID int
}

func NewPictureRepository(filePath string) *PictureRepository {
	return &PictureRepository{
		FilePath:       filePath,
		LastAssignedID: 28,
	}
}

func (r *PictureRepository) GetAllPictures() ([]Picture, error) {
	data, err := os.ReadFile(r.FilePath)
	if err != nil || len(data) == 0 {
		defaultPictures := r.getDefaultPictures()

		err = r.saveToFile(defaultPictures)
		if err != nil {
			return nil, err
		}

		return defaultPictures, nil
	}

	var imagesData struct {
		Pictures       []Picture
		LastAssignedID int
	}
	err = json.Unmarshal(data, &imagesData)
	if err != nil {
		return nil, err
	}

	r.LastAssignedID = imagesData.LastAssignedID

	return imagesData.Pictures, nil
}

func (r *PictureRepository) GetPictureByID(id string) (*Picture, error) {
	images, err := r.GetAllPictures()
	if err != nil {
		return nil, err
	}

	for _, image := range images {
		if image.ID == id {
			return &image, nil
		}
	}

	return nil, nil
}

func (r *PictureRepository) CreatePicture(image Picture) (*Picture, error) {
	images, err := r.GetAllPictures()
	if err != nil {
		return nil, err
	}

	r.LastAssignedID++
	image.ID = "picture" + strconv.Itoa(r.LastAssignedID)

	images = append(images, image)

	err = r.saveToFile(images)
	if err != nil {
		return nil, err
	}

	return &image, nil
}

func (r *PictureRepository) UpdatePicture(id string, image Picture) (*Picture, error) {
	images, err := r.GetAllPictures()
	if err != nil {
		return nil, err
	}

	var found = false

	for i, img := range images {
		if img.ID == id {
			image.ID = img.ID
			images[i] = image
			found = true
			break
		}
	}

	if found {
		err = r.saveToFile(images)
		if err != nil {
			return nil, err
		}

		return &image, nil
	}

	return nil, nil

}

func (r *PictureRepository) PatchPicture(id string, image Picture) (*Picture, error) {
	images, err := r.GetAllPictures()
	if err != nil {
		return nil, err
	}

	var found = false

	for i, img := range images {
		if img.ID == id {
			image.ID = img.ID
			if &image.Name == nil || image.Name == "" {
				image.Name = img.Name
			}
			if &image.Type == nil || image.Type == "" {
				image.Type = img.Type
			}
			if &image.URL == nil || image.URL == "" {
				image.URL = img.URL
			}
			images[i] = image
			found = true
			break
		}
	}

	if found {
		err = r.saveToFile(images)
		if err != nil {
			return nil, err
		}

		return &image, nil
	}

	return nil, nil

}

func (r *PictureRepository) DeletePicture(id string) (*Picture, error) {
	images, err := r.GetAllPictures()
	if err != nil {
		return nil, err
	}

	var image *Picture = nil

	var found = false

	for i, img := range images {
		if img.ID == id {
			image = &img
			images = append(images[:i], images[i+1:]...)
			found = true
			break
		}
	}

	if found {
		err = r.saveToFile(images)
		if err != nil {
			return nil, err
		}

		return image, nil
	}

	return nil, nil
}

func (r *PictureRepository) Reset() error {

	images := r.getDefaultPictures()
	r.LastAssignedID = 28
	err := r.saveToFile(images)

	return err
}

func (r *PictureRepository) saveToFile(images []Picture) error {
	data, err := json.Marshal(struct {
		Pictures       []Picture
		LastAssignedID int
	}{
		Pictures:       images,
		LastAssignedID: r.LastAssignedID,
	})
	if err != nil {
		return err
	}

	err = os.WriteFile(r.FilePath, data, 0644)
	if err != nil {
		return err
	}

	return nil
}

func (r *PictureRepository) getDefaultPictures() []Picture {
	return []Picture{
		{ID: "picture1", Name: "picture of Marguerite", Type: "Animal", URL: "/pictures/animals/picture1.png"},
		{ID: "picture2", Name: "picture of Clarabelle", Type: "Animal", URL: "/pictures/animals/picture2.png"},
		{ID: "picture3", Name: "picture of Bessie", Type: "Animal", URL: "/pictures/animals/picture3.png"},
		{ID: "picture4", Name: "picture of La vache qui rit", Type: "Animal", URL: "/pictures/animals/picture4.png"},
		{ID: "picture5", Name: "picture of Chicken Run", Type: "Animal", URL: "/pictures/animals/picture5.png"},
		{ID: "picture6", Name: "picture of Chicken Little", Type: "Animal", URL: "/pictures/animals/picture6.png"},
		{ID: "picture7", Name: "picture of Footix", Type: "Animal", URL: "/pictures/animals/picture7.png"},
		{ID: "picture8", Name: "picture of Monique", Type: "Animal", URL: "/pictures/animals/picture8.png"},
		{ID: "picture9", Name: "picture of Dolly", Type: "Animal", URL: "/pictures/animals/picture9.png"},
		{ID: "picture10", Name: "picture of Shaun", Type: "Animal", URL: "/pictures/animals/picture10.png"},
		{ID: "picture11", Name: "picture of Shrek", Type: "Animal", URL: "/pictures/animals/picture11.png"},
		{ID: "picture12", Name: "picture of Wooloo", Type: "Animal", URL: "/pictures/animals/picture12.png"},
		{ID: "picture13", Name: "picture of Tornado", Type: "Animal", URL: "/pictures/animals/picture13.png"},
		{ID: "picture14", Name: "picture of Jolly Jumper", Type: "Animal", URL: "/pictures/animals/picture14.png"},
		{ID: "picture15", Name: "picture of Epona", Type: "Animal", URL: "/pictures/animals/picture15.png"},
		{ID: "picture16", Name: "picture of My little Pony", Type: "Animal", URL: "/pictures/animals/picture16.png"},
		{ID: "picture17", Name: "picture of Babe", Type: "Animal", URL: "/pictures/animals/picture17.png"},
		{ID: "picture18", Name: "picture of Spider-Ham", Type: "Animal", URL: "/pictures/animals/picture18.png"},
		{ID: "picture19", Name: "picture of Naf-Naf", Type: "Animal", URL: "/pictures/animals/picture19.png"},
		{ID: "picture20", Name: "picture of Peppa Pig", Type: "Animal", URL: "/pictures/animals/picture20.png"},
		{ID: "picture21", Name: "picture of Gédéon", Type: "Animal", URL: "/pictures/animals/picture21.png"},
		{ID: "picture22", Name: "picture of Saturnin", Type: "Animal", URL: "/pictures/animals/picture22.png"},
		{ID: "picture23", Name: "picture of Donald", Type: "Animal", URL: "/pictures/animals/picture23.png"},
		{ID: "picture24", Name: "picture of Howard", Type: "Animal", URL: "/pictures/animals/picture24.png"},
		{ID: "picture25", Name: "picture of Alice", Type: "Farmer", URL: "/pictures/animals/picture25.png"},
		{ID: "picture26", Name: "picture of Bob", Type: "Farmer", URL: "/pictures/animals/picture26.png"},
		{ID: "picture27", Name: "picture of Eve", Type: "Farmer", URL: "/pictures/animals/picture27.png"},
		{ID: "picture28", Name: "picture of Mallory", Type: "Farmer", URL: "/pictures/animals/picture28.png"},
	}
}
