package ua.training.controller.command;

import ua.training.constants.AttributeNames;
import ua.training.constants.PageNames;
import ua.training.constants.ResponseMessages;
import ua.training.model.entity.User;
import ua.training.model.service.UserDaoService;
import ua.training.controller.util.DataValidator;
import ua.training.controller.util.ResourceBundleUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

public class Registration implements Command {

    @Override
    public String execute(HttpServletRequest request) {
        String login = request.getParameter(AttributeNames.LOGIN);
        String name = request.getParameter(AttributeNames.NAME);
        String password = request.getParameter(AttributeNames.PASSWORD);
        String confirmPassword = request.getParameter(AttributeNames.CONFIRM_PASSWORD);

        if (DataValidator.parameterIsEmptyOrNull(login, name, password, confirmPassword)) {
            setErrorMessage(request, ResponseMessages.REGISTRATION_NOT_ALL_FIELDS);
            return PageNames.REGISTRATION;
        }

        if(!DataValidator.passwordsAreEquals(password, confirmPassword)) {
            setErrorMessage(request, ResponseMessages.REGISTRATION_PASSWORDS_DONT_MATCH);
            return PageNames.REGISTRATION;
        }

        UserDaoService userService = new UserDaoService();

        if (userService.userExists(login)) {
            setErrorMessage(request, ResponseMessages.REGISTRATION_USER_EXISTS);
            return PageNames.REGISTRATION;
        }

        userService.create(login, password, User.Role.USER, name);

        return PageNames.LOGIN;
    }

    private void setErrorMessage(HttpServletRequest request, String errorMessage) {
        request.setAttribute(AttributeNames.WRONG_INPUT_MESSAGE, ResourceBundleUtil.getPropertyFromLangBundle(
                errorMessage, (Locale)request.getSession().getAttribute(AttributeNames.LANGUAGE)));
    }
}