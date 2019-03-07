module FixedStrings exposing (fetch, read, get)

import Array
import Http
import Url.Builder as Build
import Json.Decode as Decode
import Json.Decode.Pipeline as Pipeline
import Types as T
import FixedStringsConstants as Fsc


makeURL : String -> String -> String
makeURL server language =
    let
        versionQuery =
            Build.int "version" Fsc.version

        languageQuery =
            Build.string "language" language
    in
        Build.crossOrigin server [ "fixedstrings" ] [ versionQuery, languageQuery ]


fetch : String -> String -> Cmd T.Msg
fetch target language =
    Http.get
        { url = makeURL target language
        , expect = Http.expectJson T.GotFixedStrings (Decode.array Decode.string)
        }


read : Result Http.Error (Array.Array String) -> T.Model -> ( T.Model, Cmd T.Msg )
read result model =
    case Debug.log "fixed" result of
        Ok fixedArray ->
            ( { model | fixedStrings = fixedArray }, Cmd.none )

        Err _ ->
            ( model, Cmd.none )


get : T.Model -> Int -> String
get model index =
    Maybe.withDefault "Fixed String error" (Array.get index model.fixedStrings)
