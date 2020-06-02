import org.reflections.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;
import org.apache.log4j.Logger;


public class MathServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(MathServlet.class.getName());

    private Set<Class<?>> annotatedClasses;

    private Map <Class<?>, Object> clazzToinstances;


    private int hiddenNumber = 1;

    @Override
    public void init() throws ServletException {
        clazzToinstances = new HashMap<>();
        Reflections reflections = new Reflections("");
        annotatedClasses = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> clazz : annotatedClasses) {
            try {
                Constructor<?> constructor = clazz.getConstructor();
                Object obj = constructor.newInstance();
                clazzToinstances.put(clazz, obj);
            } catch (NoSuchMethodException e) {
                LOGGER.error(e);
            } catch (IllegalAccessException e) {
                LOGGER.error(e);
            } catch (InstantiationException e) {
                LOGGER.error(e);
            } catch (InvocationTargetException e) {
                LOGGER.error(e);
            }

        }
        LOGGER.info("Number of classes Annotated with Controller is: " + annotatedClasses.size());
    }


    private void addressToMethodsInController(String methodName, String parameterName, String methodType, int number, HttpServletResponse resp) throws IOException {
        for (Class<?> clazz : annotatedClasses) {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    MyMethod me = method.getAnnotation(MyMethod.class);
                    if (me.type().equals(methodType)) {
                        Parameter[] parameters = method.getParameters();
                        for (Parameter parameter : parameters) {
                            ArgName argName = parameter.getAnnotation(ArgName.class);
                            Object classInstance = clazzToinstances.get(clazz);
                            Object response = null;
                            try {
                                if (argName.name().equals(parameterName)) {
                                    response = method.invoke(classInstance, number);
                                    if(methodType.equals("GET")) {
                                        resp.getWriter().write("It is " + response.toString() + " that " + hiddenNumber + " divides " + number);
                                    }
                                    else {
                                        hiddenNumber = number;
                                        LOGGER.info("hiddenNumber equals to " + hiddenNumber);
                                        resp.getWriter().write("Hidden value changed to " + hiddenNumber);
                                    }
                                } else {
                                    LOGGER.info("no value provided for the parameter named " + argName.name());
                                }
                            } catch (IllegalAccessException e) {
                                LOGGER.error(e);
                            } catch (InvocationTargetException e) {
                                LOGGER.error(e);
                            }
                        }
                    } else {
                        LOGGER.info(methodType + " not supported on the method named " + methodName);
                        resp.getWriter().write("GET not supported");
                    }
                }
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.info("executing GET method");
        String name = req.getParameter("name");
        String inputNumberStr = req.getParameter("inputNumber");
        int inputNumber = 0;
        if(inputNumberStr == null || name == null) {
            LOGGER.info("Did not get required parameters " + "name is " + name + " and inputNumber is" + inputNumberStr);
            resp.getWriter().write("invalid parameters");
            return;
        }
        try {
            inputNumber = Integer.valueOf(inputNumberStr);
        } catch (NumberFormatException e) {
            LOGGER.error(e);
            resp.getWriter().write("Please provide valid number input");
            return;
        }

        addressToMethodsInController(name, "inputNumber", "GET", inputNumber, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.info("executing POST method");
        String name = req.getParameter("name");
        String valueStr = req.getParameter("value");
        int value = 0;
        if (valueStr == null || name == null) {
            LOGGER.info("Did not get required parameters " + "name is " + name + " and value is" + valueStr);
            resp.getWriter().write("invalid parameters");
            return;
        }
        try {
            value = Integer.valueOf(valueStr);
            if(value == 0) {
                LOGGER.info("passed value is invalid: " + value);
                resp.getWriter().write("0 is not allowed");
                return;
            }
        } catch (NumberFormatException e) {
            LOGGER.error(e);
            resp.getWriter().write("Please provide valid number input");
            return;
        }

        addressToMethodsInController(name, "value", "POST", value, resp);

    }
}
