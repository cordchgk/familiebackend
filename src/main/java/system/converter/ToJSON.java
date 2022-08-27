package system.converter;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.validation.constraints.NotNull;


public class ToJSON {


    public static @NotNull String toJSON(Object o) {

        ObjectMapper mapper = new ObjectMapper();

        String json = null;
        try {

            json = mapper.writeValueAsString(o);

        } catch(JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static @NotNull String customObjectMappertoJSON(Object o,ObjectMapper objectMapper) {



        String json = null;
        try {

            json = objectMapper.writeValueAsString(o);

        } catch(JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }



}

