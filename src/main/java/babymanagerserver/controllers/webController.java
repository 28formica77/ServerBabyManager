package babymanagerserver.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.web.bind.annotation.RequestMethod.GET;


@Controller
public class webController {
 

	@RequestMapping(value = "/", method = GET)
	public @ResponseBody String homepage() {
		return "BANANAAAAAAAAAA";
	}

	@RequestMapping(value = "/potato", method = GET)
	public @ResponseBody String potato() {
		return "POTATOOOOOOOOOOOOO";
	}

}
