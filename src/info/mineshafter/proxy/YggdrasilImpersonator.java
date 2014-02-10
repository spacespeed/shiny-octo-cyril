package info.mineshafter.proxy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.Proxy;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import info.mineshafter.Util;

import com.creatifcubed.simpleapi.SimpleHTTPRequest;
import com.google.gson.Gson;

public class YggdrasilImpersonator {
	private List<Profile> profiles;
	private Gson gson;
	private String authServer;

	public YggdrasilImpersonator(String authserver) {
		
		authServer = authserver;
	}

	public String authenticate(YggdrasilRequest req) {
		SimpleHTTPRequest request;
		
		request = new SimpleHTTPRequest("http://" + authServer + "/authenticate");
		request.addPost("username", req.username);
		request.addPost("password", req.password);
		request.addPost("clientToken", req.clientToken);
		String response = new String(request.doPost(Proxy.NO_PROXY),
				Charset.forName("utf-8"));
		return response;
	}

	public String refresh(YggdrasilRequest req) {
		SimpleHTTPRequest request;
		request = new SimpleHTTPRequest("http://" + authServer + "/refresh");
		request.addPost("clientToken", req.clientToken);
		request.addPost("accessToken", req.accessToken);
		String response = new String(request.doPost(Proxy.NO_PROXY),
				Charset.forName("utf-8"));
		return response;
	}

	public String invalidate(YggdrasilRequest req) {
		SimpleHTTPRequest request;
		request = new SimpleHTTPRequest("http://" + authServer + "/invalidate");
		request.addPost("clientToken", req.clientToken);
		request.addPost("accessToken", req.accessToken);
		String response = new String(request.doPost(Proxy.NO_PROXY),
				Charset.forName("utf-8"));
		return response;
	}
	
	public String checkserver(String data)
	{
		SimpleHTTPRequest request;
		request = new SimpleHTTPRequest("http://" + authServer + "/game/checkserver");
		request.addGet(data);
		String ms2Response = new String(request.doGet(Proxy.NO_PROXY),
				Charset.forName("utf-8"));
		if (ms2Response.equals("YES")) {
			return "YES";
		}
		return ms2Response;
	}
	
	public String joinserver(String data)
	{
		SimpleHTTPRequest request;
		request = new SimpleHTTPRequest("http://" + authServer + "/game/joinserver");
		request.addGet(data);
		String ms2Response = new String(request.doGet(Proxy.NO_PROXY),
				Charset.forName("utf-8"));
		if (ms2Response.equals("OK")) {
			return "OK";
		}
		return ms2Response;
	}

	public class ProfilesJSON {
		public Map<String, Profile> profiles;
		public String selectedProfile;
		public String clientToken;
		public Map<String, Profile> authenticationDatabase;
	}

	public class Profile {
		public String username;
		public String accessToken;
		public String uuid;
		public String displayName;

		public String name;
		public String playerUUID;

		public Profile(String u, String t, String id, String d) {
			username = u;
			accessToken = t;
			uuid = id;
			displayName = d;
		}
	}

	public class ProfileResponse {
		public String id;
		public String name;

		public ProfileResponse(String id, String name) {
			this.id = id;
			this.name = name;
		}
	}

	public class YggdrasilAuthResponse {
		public String accessToken;
		public String clientToken;
		public ProfileResponse selectedProfile;
		public ArrayList<ProfileResponse> availableProfiles;

		public YggdrasilAuthResponse(String clientToken, String accessToken, ProfileResponse selected, ProfileResponse available) {
			this.clientToken = clientToken;
			this.accessToken = accessToken;
			this.selectedProfile = selected;
			if (available != null) {
				this.availableProfiles = new ArrayList<ProfileResponse>();
				this.availableProfiles.add(available);
			} else this.availableProfiles = null;
		}
	}
}
