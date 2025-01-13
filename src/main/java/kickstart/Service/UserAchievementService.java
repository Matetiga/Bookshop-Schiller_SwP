package kickstart.Service;

import org.salespointframework.useraccount.Role;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import kickstart.user.User;
import kickstart.user.UserManagement;
import kickstart.Achievement.Achievement;
import org.springframework.ui.Model;



@Service
public class UserAchievementService {

	private final UserManagement userManagement;

	public UserAchievementService(UserManagement userManagement) {
		this.userManagement = userManagement;
	}

	public void processAchievement(UserDetails userDetails, Achievement achievement, Model model) {
		this.nullCheck(userDetails, model);
		if (achievement == null) {
			throw new NullPointerException("Achievement null");
		}

		User user = this.getUserOrNullException(userDetails);

		if (!user.hasAchievement(achievement) && user.achievementCanBeAdded(achievement)){
			userManagement.addAchievementToUser(user, achievement);
			//in know its always false but im desperate
			if (achievement == null){
				throw new NullPointerException("Achievement null");
			}
			model.addAttribute("achievement", achievement);
			System.out.println("Achievement in controller: " + achievement);

			return;
		}
		System.out.println("Achievement in controller: " + "NONE");
	}

	public void addAllAchievementsToModel(UserDetails userDetails, Model model){
		this.nullCheck(userDetails, model);
		User user = this.getUserOrNullException(userDetails);

		model.addAttribute("achievements", user.getAchievements());

	}

	public void addProgressToModel(UserDetails userDetails, Model model){
		this.nullCheck(userDetails, model);
		User user = this.getUserOrNullException(userDetails);

		int percentageCompletedAchievements = (user.getAchievements().size() * 100) / 10;
		model.addAttribute("progress", percentageCompletedAchievements);
	}

	private void nullCheck(UserDetails userDetails, Model model){
		if (userDetails == null) {
			throw new NullPointerException("userDetails is null");
		}
		if (model == null){
			throw new NullPointerException("Model null");
		}
	}

	private User getUserOrNullException(UserDetails userDetails){
		User user = userManagement.findByUserDetails(userDetails);

		if (user == null) {
			throw new NullPointerException("User null");
		}

		return user;
	}

	public UserDetails getCurrentUser(){
		return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
}
