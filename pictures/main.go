package main

import (
	"log"
	"net/http"
	"os"

	"github.com/gorilla/mux"
)

func main() {
	repository := NewPictureRepository("resources/db.txt")
	service := NewPictureService(repository)
	controller := NewPictureController(service)

	router := mux.NewRouter()

	router.HandleFunc("/pictures", controller.GetPictures).Methods("GET")
	router.HandleFunc("/pictures/{id}", controller.GetPicture).Methods("GET")
	router.HandleFunc("/pictures", controller.CreatePicture).Methods("POST")
	router.HandleFunc("/pictures/{id}", controller.UpdatePicture).Methods("PUT")
	router.HandleFunc("/pictures/{id}", controller.PatchPicture).Methods("PATCH")
	router.HandleFunc("/pictures/{id}", controller.DeletePicture).Methods("DELETE")
	router.HandleFunc("/reset", controller.Reset).Methods("POST")

	var hostAndPort = "localhost:5000"
	if len(os.Args) > 1 {
		hostAndPort = os.Args[1]
	}

	log.Println("Go Server (" + hostAndPort + ") started")
	log.Print(http.ListenAndServe(hostAndPort, router))
}
