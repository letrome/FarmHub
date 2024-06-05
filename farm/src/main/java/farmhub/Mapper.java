package farmhub;

import farmhub.clients.model.AnimalClientModel;
import farmhub.clients.model.FarmerClientModel;
import farmhub.clients.model.PictureClientModel;
import farmhub.clients.model.SpecieClientModel;
import farmhub.repository.models.FarmRepositoryModel;
import farmhub.service.models.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Mapper {

    public static FarmModel toFarmModel(FarmRepositoryModel farmRepositoryModel, List<AnimalClientModel> animalClientModels, List<SpecieClientModel> specieClientModels, List<PictureClientModel> pictureClientModels, List<FarmerClientModel> farmerClientModels, boolean includeGone){
        List<AnimalModel> animalModels = Mapper.toAnimalModels(animalClientModels, specieClientModels, pictureClientModels, farmerClientModels, includeGone);
        List<FarmerModel> farmerModels = Mapper.toFarmerModels(farmerClientModels, pictureClientModels, includeGone);

        return FarmModel.builder()
                .farmName(farmRepositoryModel.getFarmName())
                .beginningDate(farmRepositoryModel.getBeginningDate())
                .animals(animalModels)
                .nbAnimals(animalModels.size())
                .farmers(farmerModels)
                .nbFarmers(farmerModels.size())
                .build();
    }

    public static List<AnimalModel> toAnimalModels(List<AnimalClientModel> animalClientModels, List<SpecieClientModel> specieClientModels, List<PictureClientModel> pictureClientModels, List<FarmerClientModel> farmerClientModels, boolean includeGone) {
        return animalClientModels.stream()
                .filter(model -> includeGone || !"gone".equals(model.getStatus()))
                .map(animalClientModel -> {

                    SpecieModel specieModel = findSpecie(specieClientModels, animalClientModel.getSpecie())
                            .map(Mapper::toSpecieModel)
                            .orElse(null);

                    Optional<PictureClientModel> pictureClientModel = findPicture(pictureClientModels, animalClientModel.getPicture());
                    PictureModel pictureModel = pictureClientModel
                            .map(Mapper::toPictureModel)
                            .orElse(null);

                    FarmerModel farmerModel = findFarmer(farmerClientModels, animalClientModel.getFarmer())
                            .map(model -> toFarmerModel(model, findPicture(pictureClientModels, model.getPicture()).orElse(null)))
                            .orElse(null);


                    return AnimalModel.builder()
                            .id(animalClientModel.getId())
                            .name(animalClientModel.getName())
                            .birthDate(animalClientModel.getBirthDate())
                            .specie(specieModel)
                            .farmer(farmerModel)
                            .status(animalClientModel.getStatus())
                            .picture(pictureModel)
                            .build();
                })
                .collect(Collectors.toList());
    }

    public static List<FarmerModel> toFarmerModels(List<FarmerClientModel> farmerClientModels, List<PictureClientModel> pictureClientModels, boolean includeGone) {
        return farmerClientModels.stream()
                .filter(model -> includeGone || !"gone".equals(model.getStatus()))
                .map(model -> {
                    PictureModel pictureModel = findPicture(pictureClientModels, model.getPicture())
                            .map(Mapper::toPictureModel)
                            .orElse(null);
                    return FarmerModel.builder()
                            .id(model.getId())
                            .firstName(model.getFirstName())
                            .lastName(model.getLastName())
                            .birthDate(model.getBirthDate())
                            .specialties(model.getSpecialties())
                            .picture(pictureModel)
                            .status(model.getStatus())
                            .build();
                })
                .collect(Collectors.toList());
    }

    public static AnimalModel toAnimalModel(AnimalClientModel animalClientModel, SpecieClientModel specieClientModel, FarmerClientModel farmerClientModel, PictureClientModel farmerPictureClientModel, PictureClientModel pictureClientModel) {
        SpecieModel specieModel = toSpecieModel(specieClientModel);
        FarmerModel farmerModel = toFarmerModel(farmerClientModel, farmerPictureClientModel);
        PictureModel pictureModel = toPictureModel(pictureClientModel);

        return AnimalModel.builder()
                .id(animalClientModel.getId())
                .name(animalClientModel.getName())
                .birthDate(animalClientModel.getBirthDate())
                .specie(specieModel)
                .farmer(farmerModel)
                .status(animalClientModel.getStatus())
                .picture(pictureModel)
                .build();
    }

    public static SpecieModel toSpecieModel(SpecieClientModel specieClientModel) {
        return SpecieModel.builder()
                .id(specieClientModel.getId())
                .name(specieClientModel.getName())
                .description(specieClientModel.getDescription())
                .diet(specieClientModel.getDiet())
                .naturalHabitat(specieClientModel.getNaturalHabitat())
                .conservationStatus(specieClientModel.getConservationStatus())
                .build();
    }

    public static FarmerModel toFarmerModel(FarmerClientModel farmerClientModel, PictureClientModel pictureClientModel) {
        PictureModel pictureModel = toPictureModel(pictureClientModel);
        return FarmerModel.builder()
                .id(farmerClientModel.getId())
                .firstName(farmerClientModel.getFirstName())
                .lastName(farmerClientModel.getLastName())
                .birthDate(farmerClientModel.getBirthDate())
                .specialties(farmerClientModel.getSpecialties())
                .picture(pictureModel)
                .status(farmerClientModel.getStatus())
                .build();
    }

    public static PictureModel toPictureModel(PictureClientModel pictureClientModel) {
        return PictureModel.builder()
                .id(pictureClientModel.getId())
                .name(pictureClientModel.getName())
                .type(pictureClientModel.getType())
                .url(pictureClientModel.getUrl())
                .build();
    }

    private static Optional<FarmerClientModel> findFarmer(List<FarmerClientModel> farmerClientModels, String farmerId) {
        return farmerClientModels.stream()
                .filter(model -> farmerId.equals(model.getId()))
                .findFirst();
    }

    private static Optional<PictureClientModel> findPicture(List<PictureClientModel> pictureClientModels, String pictureId) {
        return pictureClientModels.stream()
                .filter(model -> pictureId.equals(model.getId()))
                .findFirst();
    }

    private static Optional<SpecieClientModel> findSpecie(List<SpecieClientModel> specieClientModels, String specieId) {
        return specieClientModels.stream()
                .filter(model -> specieId.equals(model.getId()))
                .findFirst();
    }
}
