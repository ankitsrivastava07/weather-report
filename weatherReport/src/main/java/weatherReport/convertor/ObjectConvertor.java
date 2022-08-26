package weatherReport.convertor;
import org.modelmapper.ModelMapper;
public class ObjectConvertor {
    private static final ModelMapper mapper = new ModelMapper();

    public static <D> D convert(Object source, Class<D> target){
        return mapper.map(source,target);
    }
}
