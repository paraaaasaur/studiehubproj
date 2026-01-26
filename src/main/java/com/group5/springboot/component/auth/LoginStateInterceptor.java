package com.group5.springboot.component.auth;

import com.group5.springboot.annotation.auth.RejectsAdmin;
import com.group5.springboot.annotation.auth.RequiresAdmin;
import com.group5.springboot.annotation.auth.RejectsUser;
import com.group5.springboot.annotation.auth.RequiresUser;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Temporary legacy access-control interceptor for centralized authentication.
 * Will be replaced by Spring Security in/after 2.0.0.
 */
@Component
public class LoginStateInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (handler instanceof HandlerMethod) {
			HandlerMethod method = (HandlerMethod) handler;

			boolean requiresUser = method.hasMethodAnnotation(RequiresUser.class);
			boolean rejectsUser = method.hasMethodAnnotation(RejectsUser.class);
			boolean requiresAdmin = method.hasMethodAnnotation(RequiresAdmin.class);
			boolean rejectsAdmin = method.hasMethodAnnotation(RejectsAdmin.class);

			// fast failure
			if (!requiresUser && !rejectsUser && !requiresAdmin && !rejectsAdmin) {
				return true;
			}

			HttpSession session = request.getSession(false);
			boolean userLoggedIn = session != null && session.getAttribute("loginBean") != null;
			boolean adminLoggedIn = session != null && session.getAttribute("adminId") != null;

			boolean requiresUserViolated = requiresUser && !userLoggedIn;
			boolean rejectsUserViolated = rejectsUser && userLoggedIn;
			boolean requiresAdminViolated = requiresAdmin && !adminLoggedIn;
			boolean rejectsAdminViolated = rejectsAdmin && adminLoggedIn;

			// for rest endpoints
			if (isRestEndpoint(method)) {
				if (requiresUserViolated || requiresAdminViolated) {
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
					return false;
				}
				if (rejectsUserViolated || rejectsAdminViolated) {
					response.sendError(HttpServletResponse.SC_FORBIDDEN);
					return false;
				}
			}
			// for page-rendering endpoints
			else {
				if (requiresUserViolated) {
					request.getRequestDispatcher("/gotologin.controller").forward(request, response);
	//				response.sendRedirect("/gotologin.controller");
					return false;
				}
				if (rejectsUserViolated) {
					request.getRequestDispatcher("/").forward(request, response);
	//				response.sendRedirect("/");
					return false;
				}
				if (requiresAdminViolated) {
					request.getRequestDispatcher("/gotoAdminLogin.controller").forward(request, response);
	//				response.sendRedirect("/gotoAdminLogin.controller");
					return false;
				}
				if (rejectsAdminViolated) {
					request.getRequestDispatcher("/gotoAdminIndex.controller").forward(request, response);
	//				response.sendRedirect("/gotoAdminIndex.controller");
					return false;
				}
			}
		}

		return true;
	}


	// helpers
	private static boolean isRestEndpoint(HandlerMethod handlerMethod) {
		var controllerClazz = handlerMethod.getMethod().getDeclaringClass();

		boolean c1 = handlerMethod.hasMethodAnnotation(ResponseBody.class);
		boolean c2 = controllerClazz.isAnnotationPresent(RestController.class);
		boolean c3 = controllerClazz.isAnnotationPresent(ResponseBody.class);
		boolean c4 = handlerMethod.getMethod().isAnnotationPresent(ResponseBody.class);

		return c1 || c2 || c3 || c4;
	}
}