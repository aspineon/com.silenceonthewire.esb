package parsers.core;

import akka.util.ByteString;
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.F;
import play.libs.streams.Accumulator;
import play.mvc.BodyParser;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
 
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.reflect.ParameterizedType;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
 
public abstract class JsonBodyParserWithValidation<T> implements BodyParser<T> {
 
    @Inject
    private BodyParser.Json jsonParser;
    @Inject
    private Executor executor;
 
    private final Class<T> clazz = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
 
    @Override
    public Accumulator<ByteString, F.Either<Result, T>> apply(Http.RequestHeader request) {
        
        Accumulator<ByteString, F.Either<Result, JsonNode>> jsonAccumulator = jsonParser.apply(request);
        
        return jsonAccumulator.map(jsonOrError -> {
            if(jsonOrError.left.isPresent()) {
                return F.Either.Left(jsonOrError.left.get());
            } else {
                JsonNode jsonNode = jsonOrError.right.get();
                try {
                    T jsonObject = play.libs.Json.fromJson(jsonNode,clazz);
                    final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
                    final Validator validator = validatorFactory.getValidator();
 
                    Set<ConstraintViolation<T>> validationResult = validator.validate(jsonObject);
                    if(validationResult.isEmpty()) {
                        return F.Either.Right(jsonObject);
                    } else {
                        return F.Either.Left(Results.badRequest(play.libs.Json.toJson(validationResult.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList()))));
                    }
                } catch (Exception exception) {
                    return F.Either.Left(Results.badRequest("zle!"));
                }
            }
        },executor);
    }
}