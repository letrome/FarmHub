package main

import "encoding/json"

type Picture struct {
	ID    string          `json:"id"`
	Name  string          `json:"name"`
	Type  string          `json:"type"`
	URL   string          `json:"url"`
	Extra json.RawMessage `json:"extra,omitempty"`
}
