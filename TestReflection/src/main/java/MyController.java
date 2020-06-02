
@Controller
public class MyController {

    private int value = 1;

    @MyMethod(type = "GET")
    public boolean dividesValue(@ArgName(name = "inputNumber")int a) {
        return a % value == 0;
    }

    @MyMethod(type = "POST")
    public void updateValue(@ArgName(name = "value")int value) {
        this.value = value;
    }
}
