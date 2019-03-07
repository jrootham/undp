module Types exposing (..)

import Array
import Http


type alias Model =
    { server : String
    , fixedStrings : Array.Array String
    }


type Msg
    = GotFixedStrings (Result Http.Error (Array.Array String))
