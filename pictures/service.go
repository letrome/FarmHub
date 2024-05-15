package main

type PictureService struct {
	Repository *PictureRepository
}

func NewPictureService(repository *PictureRepository) *PictureService {
	return &PictureService{
		Repository: repository,
	}
}

func (s *PictureService) GetAllPictures() ([]Picture, error) {
	return s.Repository.GetAllPictures()
}

func (s *PictureService) GetPictureByID(id string) (*Picture, error) {
	return s.Repository.GetPictureByID(id)
}

func (s *PictureService) CreatePicture(image Picture) (*Picture, error) {
	return s.Repository.CreatePicture(image)
}

func (s *PictureService) UpdatePicture(id string, image Picture) (*Picture, error) {
	return s.Repository.UpdatePicture(id, image)
}

func (s *PictureService) PatchPicture(id string, image Picture) (*Picture, error) {
	return s.Repository.PatchPicture(id, image)
}

func (s *PictureService) DeletePicture(id string) (*Picture, error) {
	return s.Repository.DeletePicture(id)
}

func (s *PictureService) Reset() error {
	return s.Repository.Reset()
}
