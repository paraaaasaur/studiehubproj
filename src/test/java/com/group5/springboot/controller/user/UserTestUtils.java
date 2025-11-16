package com.group5.springboot.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group5.springboot.model.user.User_Info;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

public final class UserTestUtils {
	private final MockMvc mockMvc;

	public UserTestUtils(MockMvc mockMvc) {
		this.mockMvc = mockMvc;
	}

	/**
	 * <li>id = demoid2</li>
	 * <li>psw = demopsw</li>
	 * <li>firstname = majin</li>
	 * <li>lastname = boo</li>
	 * <li>email = ledges-meal.5f@icloud.com</li>
	 **/
	public static User_Info aUserDemoid2() {
		User_Info userInfo = new User_Info();
		{
			userInfo.setU_id("demoid2");
			userInfo.setU_psw("demopsw");
			userInfo.setU_firstname("majin");
			userInfo.setU_lastname("boo");
			userInfo.setU_email("ledges-meal.5f@icloud.com");
		}
		return userInfo;
	}

	public static User_Info aRandomUser() {
		User_Info userInfo = new User_Info();
		{
			Collections.shuffle(idCandidates);
			userInfo.setU_id(idCandidates.remove(0));
			Collections.shuffle(passwordCandidates);
			userInfo.setU_psw(passwordCandidates.remove(0));
			Collections.shuffle(firstnameCandidates);
			userInfo.setU_firstname(firstnameCandidates.remove(0));
			Collections.shuffle(lastnameCandidates);
			userInfo.setU_lastname(lastnameCandidates.remove(0));
			userInfo.setU_email("fake-account@foo.bar.fake-domain.com.to");
		}
		return userInfo;
	}

	public static User_Info aUserJoshua() {
		User_Info joshua = new User_Info();
		{
			joshua.setU_id("joshua");
			joshua.setU_psw(UUID.randomUUID().toString());
			joshua.setU_firstname("Jo-An");
			joshua.setU_lastname("Sun");
			joshua.setU_email("overloaded-overlord@icloud.com");
		}

		return joshua;
	}

	public static User_Info aUserKen() {
		User_Info ken = new User_Info();
		{
			ken.setU_id("ken");
			ken.setU_psw(UUID.randomUUID().toString());
			ken.setU_firstname("Yu-Chi");
			ken.setU_lastname("Huang");
			ken.setU_email("married-gentleman-5125@tuta.io");
		}

		return ken;
	}

	public static User_Info aUserTajenwww() {
		User_Info tajenwww = new User_Info();
		{
			tajenwww.setU_id("tajenwww");
			tajenwww.setU_psw(UUID.randomUUID().toString());
			tajenwww.setU_firstname("Ta-Jen");
			tajenwww.setU_lastname("Wang");
			tajenwww.setU_email("furious-tomato-pasta@github.io");
		}

		return tajenwww;
	}

	public static User_Info aUserYuz() {
		User_Info yuz = new User_Info();
		{
			yuz.setU_id("yuz");
			yuz.setU_psw(UUID.randomUUID().toString());
			yuz.setU_firstname("Yu-Tse");
			yuz.setU_lastname("Tu");
			yuz.setU_email("king-of-domains@google.com");
		}

		return yuz;
	}

	public static User_Info aUserNick() {
		User_Info nick = new User_Info();
		{
			nick.setU_id("nick");
			nick.setU_psw(UUID.randomUUID().toString());
			nick.setU_firstname("Meng-Hua");
			nick.setU_lastname("Chung");
			nick.setU_email("the-ultimate-jp-pro@yahoo.jp.co");
		}

		return nick;
	}

	public static User_Info aUserYen() {
		User_Info yen = new User_Info();
		{
			yen.setU_id("yen");
			yen.setU_psw(UUID.randomUUID().toString());
			yen.setU_firstname("Chia-Cheng");
			yen.setU_lastname("Yen");
			yen.setU_email("chitty-chatty@hotmail.com");
		}

		return yen;
	}

	/**
	 * <li>perform POST /login.controller and assert success</li>
	 * <li>pass a new {@code MockHttpSession} to apply state change</li>
	 **/
	public void loginAs(final User_Info userInfo, final MockHttpSession mockHttpSession) throws Exception {
		mockMvc.perform(post("/login.controller")
						.session(mockHttpSession)
						.contentType(APPLICATION_JSON)
						.content(new ObjectMapper().writeValueAsString(userInfo)))

				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value("登入成功"));
	}

	/**
	 * <li>admin-login as {id = adming5; psw = manager}</li>
	 * <li>perform POST /AdminLogin.controller and assert success</li>
	 * <li>pass a new {@code MockHttpSession} to apply state change</li>
	 **/
	public void adminLoginAsAdming5(final MockHttpSession mockHttpSession) throws Exception {
		String[] adminCreds = {"adming5", "manager"};

		mockMvc.perform(post("/AdminLogin.controller")
						.session(mockHttpSession)
						.contentType(APPLICATION_FORM_URLENCODED)
						.param("id", adminCreds[0])
						.param("psw", adminCreds[1]))

				.andExpect(flash().attribute("success", is("管理員登入成功")))
				.andExpect(request().sessionAttribute("adminId", is("adming5")));
	}


	// meme data
	private static final ArrayList<String> idCandidates = new ArrayList<>(List.of(
			"CritLord420", "LagWizard", "PotionTaxEvader", "AFKChad",
			"GoblinIntern", "SneakyBackstabber", "ManaStarvedMage", "ChairPaladin",
			"LootGremlin", "QuestRefuser", "XPLeech", "PetRockRanger",
			"TiltproofSamurai", "HealingPotionAddict", "NoScopeGrandma", "DungeonJanitor",
			"FishSlapMonk", "PatchNoteVictim", "404SkillNotFound", "MinMaxedPotato"
	));

	private static final List<String> passwordCandidates = new ArrayList<>(List.of(
			"SwordFish123!", "ILagIRage99", "ManaPotion4Life!", "Goblin_Gold_777",
			"AFKbrbTeaTime~", "LetMeCritPls!!", "StealthMode_Engaged", "0rangePeelPaladin", "QuestDenied2025", "2HPandVibing", "UwU_NoHitRun", "SpaghettiCodeMage1", "GigaChad_Tank_88", "DropRateIsALie!!", "NerfMeIDareYou", "DodgeRollOrDie420", "NPCdialogSkip99","NoSleepRaidNight", "RoarOfThePotato!", "AltF4Legend"
	));

	private static final List<String> firstnameCandidates = new ArrayList<>(List.of(
			"Riku", "Astra", "Thorn", "Mika", "Zane",
			"Lyria", "Kiro", "Ember", "Soren", "Kaela",
			"Riven", "Nora", "Drax", "Milo", "Aeris",
			"Fen", "Nyx", "Kai", "Rhea", "Vox"
	));

	private static final List<String> lastnameCandidates = new ArrayList<>(List.of(
			"Stormstrike", "Nightwhisper", "Ironpaw", "Cloudpiercer", "Skullbloom",
			"Riftwalker", "Brightforge", "Moonsnare", "Swiftclaw", "Blazetide",
			"Starborn", "Frostthorn", "Shadowgleam", "Sunbreaker", "Voidchant",
			"Ashweaver", "Runeblade", "Morrowfall", "Wyrmcaller", "Dustveil"
	));
}
