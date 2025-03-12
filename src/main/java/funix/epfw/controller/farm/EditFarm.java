package funix.epfw.controller.farm;

import funix.epfw.constants.ViewPaths;
import funix.epfw.model.farm.Farm;
import funix.epfw.service.farm.FarmService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class EditFarm {
    private final FarmService farmService;

    @Autowired
    public EditFarm(FarmService farmService) {
        this.farmService = farmService;
    }

    @GetMapping("/editFarm/{farmId}")
    public String editFarm (@PathVariable Long farmId, Model model, HttpSession session) {

        Farm farm = farmService.findById(farmId);
        model.addAttribute("farm", farm);

        return ViewPaths.EDIT_FARM;
    }
    @PostMapping("/editFarm/{farmId}")
    public String editFarm(@Validated @ModelAttribute("farm") Farm farmToUpDate,
                           BindingResult result, @PathVariable Long farmId,
                           Model model, HttpSession session,
                           RedirectAttributes redirectAttributes) {



        redirectAttributes.addFlashAttribute("sucessMess", "Cập nhật thông tin farm thành công");
        return ViewPaths.MANAGE_FRAM;
    }
}
