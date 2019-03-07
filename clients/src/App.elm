module App exposing (main)

import Array
import Browser
import Http
import Html exposing (Html, div, text, h1)
import Types as T
import FixedStrings as Fs
import FixedStringsConstants as Fsc


main =
    Browser.document
        { init = init
        , update = update
        , subscriptions = subscriptions
        , view = view
        }


init : String -> ( T.Model, Cmd T.Msg )
init target =
    ( { server = target, fixedStrings = Array.empty }, (Fs.fetch target "en") )


update : T.Msg -> T.Model -> ( T.Model, Cmd T.Msg )
update msg model =
    case msg of
        T.GotFixedStrings result ->
            Fs.read result model


subscriptions : T.Model -> Sub T.Msg
subscriptions model =
    Sub.none


view : T.Model -> Browser.Document T.Msg
view model =
    { body = [ text model.server ]
    , title = Fs.get model Fsc.title
    }
