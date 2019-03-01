module App exposing (main)

import Browser
import Http
import Html exposing (Html, div, text, h1)

main =
  Browser.document
    { init = init
    , update = update
    , subscriptions = subscriptions
    , view = view
    }

type alias Model =
  { server : String
  }

init: String -> (Model, Cmd Msg)
init target =
    ({server = target}, Cmd.none)

type Msg
    = GotStrings (Result Http.Error String)


update : Msg -> Model -> (Model, Cmd Msg)
update msg model =
    Debug.todo "update"

subscriptions : Model -> Sub Msg
subscriptions model =
    Sub.none

view : Model -> Browser.Document Msg
view model =
    { body = [text model.server]
    , title = "title"
    }

