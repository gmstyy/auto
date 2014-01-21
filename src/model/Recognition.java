
package model;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import utils.FileUtil;
import utils.JsonUtil;
import utils.RGBUtil;

public class Recognition extends Neure implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5184396155141746130L;
	public static String recDir="train";
	private RecMap okMap;
	private RecMap passMap;
	private String value;
	private String name;
	private double threshold = 0.618;
	private double score = 0.0;
	private double trainAdjust = 0.1;
	private String debugRgb = "sk393047-0", debugName = "font-k1";
	private Skeleton currentSk;
	public Recognition(File file) {
		name=file.getName().replaceAll(".txt", "");
		load();
	}
	public Recognition(BufferedImage model, String name,String value) {
		this.value=value;
		this.name = name;
		int height = model.getHeight();
		int width = model.getWidth();
		Skeleton okSk = new Skeleton(1, width, height);
		Skeleton passSk = new Skeleton(1, width, height);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int rgb = model.getRGB(i, j) & 0xFFFFFF;
				if (rgb != 0xFFFFFF) {
					okSk.addPix(new Pixel(i, j, rgb));
					continue;
				}
				passSk.addPix(new Pixel(i, j, Color.GRAY.getRGB()));
			}
		}
		Set<Pixel> okSet = okSk.trim();
		Set<Pixel> passSet = passSk.trim(okSk.getMinWitdh(), okSk.getMinHeight(), okSk.getMaxWidth(), okSk.getMaxHeight());
		okMap = new RecMap(okSk.getTrimWidth(), okSk.getTrimHeight());
		passMap = new RecMap(passSk.getTrimWidth(), passSk.getTrimHeight());
		for (Pixel pix : okSet) {
			okMap.put(pix, 1.0);
		}
		for (Pixel pix : passSet) {
			passMap.put(pix, 0.7);
		}
		genImg("ok-" + name, okMap);
		genImg("pass-" + name, passMap);
	}
	public void genImg(String name,RecMap recMap){
		double maxScore=0,minScore=100;
		for (Entry<Pixel,Double> en : recMap.entrySet()) {
			maxScore=Math.max(en.getValue(), maxScore);
			minScore=Math.min(en.getValue(), minScore);
		}
		double distance=maxScore-minScore;
		for (Entry<Pixel,Double> en : recMap.entrySet()) {
			en.getKey().setRgb(new Double(0x888888*(maxScore-en.getValue())/distance).intValue());
		}
		RGBUtil.genImg(FileUtil.defaultPath+"\\train",name, recMap.keySet(), recMap.width, recMap.height);
	}
	@Override
	public void stimulated(Neure neure, boolean flag) {
		RecMap okMapLocal=new RecMap(okMap.getWidth(),okMap.getHeight());
		RecMap passMapLocal=new RecMap(passMap.getWidth(),passMap.getHeight());
		okMapLocal.putAll(okMap);
		passMapLocal.putAll(passMap);
		Skeleton sk = (Skeleton) neure;
		currentSk=sk;
		Set<Pixel> trimSet = sk.getTrimSet();
		double rate = new Double(trimSet.size()) / (okMapLocal.size());
		// double passRate = getRate(sk.getTrimWidth(), sk.getTrimHeight(),
		// passMap.getWidth(), passMap.getHeight());
		double okd = 0.0, passd = 0.0;
		if (sk.getName().equals(debugRgb) && value.equals(debugName)) {
			int a = 1;
		}
		double xRate = new Double(sk.getTrimWidth()) / okMapLocal.getWidth();
		double yRate = new Double(sk.getTrimHeight()) / okMapLocal.getHeight();
		if (rate > 1) {
			for (Entry<Pixel, Double> en : okMapLocal.entrySet()) {
				Pixel pix = getTransPix(en.getKey(), xRate, yRate);
				if(trimSet.contains(pix)){
					okd += en.getValue() / okMapLocal.size();
				}
			}
			for (Entry<Pixel, Double> en : passMapLocal.entrySet()) {
				Pixel pix = getTransPix(en.getKey(), xRate, yRate);
				if(trimSet.contains(pix)){
					passd += en.getValue() / passMapLocal.size();
				}
			}
		}else{
			for (Pixel pix: trimSet) {
				Pixel newPix = getTransPix(pix, 1/xRate, 1/yRate);
				if(okMapLocal.containsKey(newPix)){
					okd += okMapLocal.get(newPix)/ (trimSet.size()) ;
					okMapLocal.remove(newPix);
				}
				if(passMapLocal.containsKey(newPix)){
					passd += passMapLocal.get(newPix)/ (trimSet.size()) ;
					passMapLocal.remove(newPix);
				}
			}
		}
		score = okd - passd;
		System.out.println("    " + sk.getName() + " " + value + " " + Integer.toHexString(sk.getRgb()) + " " + okd + " " + passd);
		this.complete();
	}

	private Pixel getTransPix(Pixel pix, double xRate, double yRate) {
		int x = new Double(pix.getX() * xRate).intValue();
		int y = new Double(pix.getY() * yRate).intValue();
		Pixel newPix = new Pixel(x, y, pix.getRgb());
		return newPix;
	}
	@Override
	public void store() {
		Map<String,Object> jsonMap= new HashMap<>();
		jsonMap.put("name", name);
		jsonMap.put("value", value);
		jsonMap.put("threshold", threshold);
		jsonMap.put("okMap", okMap);
		jsonMap.put("okMapWidth", okMap.getWidth());
		jsonMap.put("okMapHeight", okMap.getHeight());
		jsonMap.put("passMap", passMap);
		jsonMap.put("passMapWidth", passMap.getWidth());
		jsonMap.put("passMapHeight", passMap.getHeight());
		String json=JsonUtil.objectToJson(jsonMap);
		FileUtil.genFile(recDir+"\\"+name+".txt",json);
		genImg("ok-"+name, okMap);
		genImg("pass-"+name, passMap);
	}
	@Override
	public void load() {
		ObjectMapper objectMapper = new ObjectMapper();
		this.okMap=new RecMap(0, 0);
		this.passMap=new RecMap(0, 0);
		String json=FileUtil.readFile(recDir+"\\"+name+".txt");
		try {
			JsonNode root= objectMapper.readTree(json);
			JsonNode okRoot=root.get("okMap");
			Iterator<Entry<String, JsonNode>> iterator=okRoot.getFields();
			while(iterator.hasNext()){
				Entry<String, JsonNode> en=iterator.next();
				okMap.put(new Pixel(en.getKey()), Double.parseDouble(en.getValue().getValueAsText()));
			}
			JsonNode passRoot=root.get("passMap");
			iterator=passRoot.getFields();
			while(iterator.hasNext()){
				Entry<String, JsonNode> en=iterator.next();
				passMap.put(new Pixel(en.getKey()), Double.parseDouble(en.getValue().getValueAsText()));
			}
			value=root.get("value").getValueAsText();
			threshold=root.get("threshold").getDoubleValue();
			okMap.setWidth(root.get("okMapWidth").getIntValue());
			okMap.setHeight(root.get("okMapHeight").getIntValue());
			passMap.setWidth(root.get("passMapWidth").getIntValue());
			passMap.setHeight(root.get("passMapHeight").getIntValue());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void complete() {
		if (score >= threshold) {
			currentSk.getResultList().add(this);
			for(Neure neure:frontSet){
				neure.stimulated(this, true);
			}
		}else{
			for(Neure neure:frontSet){
				neure.stimulated(this, false);
			}
		}
	}

	@Override
	public void feedback(Neure neure,boolean enhance) {
		Set<Pixel> trimSet=currentSk.getTrimSet();
		double xRate = new Double(currentSk.getTrimWidth()) / okMap.getWidth();
		double yRate = new Double(currentSk.getTrimHeight()) / okMap.getHeight();
		if(enhance){
			double totalAdjust=0.0;
			double minusAdjust=0.0;
			Map<Pixel, Double> okTranMap=new HashMap<>();
			Map<Pixel, Double> passTranMap=new HashMap<>();
			for(Entry<Pixel, Double> en:okMap.entrySet()){
				Pixel newPix = getTransPix(en.getKey(), xRate,yRate);
				if(trimSet.contains(newPix)){
					double score=en.getValue()+trainAdjust;
					totalAdjust+=trainAdjust;
					okMap.put(en.getKey(), score);
				}
			}
			minusAdjust=totalAdjust/okMap.size();
			for(Entry<Pixel, Double> en:okMap.entrySet()){
				double score=en.getValue()-minusAdjust;
				if(score<0){
					okTranMap.put(en.getKey(), 0-score);
					okMap.remove(en.getKey());
					continue;
				}
				okMap.put(en.getKey(), score);
			}
			totalAdjust=0.0;
			minusAdjust=0.0;
			for(Entry<Pixel, Double> en:passMap.entrySet()){
				Pixel newPix = getTransPix(en.getKey(), xRate,yRate);
				if(trimSet.contains(newPix)){
					double score=en.getValue()-2*trainAdjust;
					totalAdjust+=2*trainAdjust;
					passMap.put(en.getKey(), score);
				}
			}
			minusAdjust=totalAdjust/passMap.size();
			for(Entry<Pixel, Double> en:passMap.entrySet()){
				double score=en.getValue()+minusAdjust;
				if(score<0){
					passTranMap.put(en.getKey(), 0-score);
					passMap.remove(en.getKey());
					continue;
				}
				passMap.put(en.getKey(), score);
			}
			okMap.putAll(passTranMap);
			passMap.putAll(okTranMap);
		}else{
			double totalAdjust=0.0;
			double minusAdjust=0.0;
			Map<Pixel, Double> okTranMap=new HashMap<>();
			Map<Pixel, Double> passTranMap=new HashMap<>();
			for(Entry<Pixel, Double> en:okMap.entrySet()){
				Pixel newPix = getTransPix(en.getKey(), xRate,yRate);
				if(trimSet.contains(newPix)){
					double score=en.getValue()-0.5*trainAdjust;
					totalAdjust+=0.5*trainAdjust;
					okMap.put(en.getKey(), score);
				}
			}
			minusAdjust=totalAdjust/okMap.size();
			for(Entry<Pixel, Double> en:okMap.entrySet()){
				double score=en.getValue()+minusAdjust;
				if(score<0){
					okTranMap.put(en.getKey(), 0-score);
					okMap.remove(en.getKey());
					continue;
				}
				okMap.put(en.getKey(), score);
			}
			totalAdjust=0.0;
			minusAdjust=0.0;
			for(Entry<Pixel, Double> en:passMap.entrySet()){
				Pixel newPix = getTransPix(en.getKey(), xRate,yRate);
				if(trimSet.contains(newPix)){
					double score=en.getValue()+0.3*trainAdjust;
					totalAdjust+=0.3*trainAdjust;
					passMap.put(en.getKey(), score);
				}
			}
			minusAdjust=totalAdjust/passMap.size();
			for(Entry<Pixel, Double> en:passMap.entrySet()){
				double score=en.getValue()-minusAdjust;
				if(score<0){
					passTranMap.put(en.getKey(), 0-score);
					passMap.remove(en.getKey());
					continue;
				}
				passMap.put(en.getKey(), score);
			}
			okMap.putAll(passTranMap);
			passMap.putAll(okTranMap);
		}
		store();
	}

	public class RecMap extends HashMap<Pixel, Double> {

		/**
		 * 
		 */
		private static final long serialVersionUID = -3098554022208193792L;
		private int width = 0, height = 0;

		public RecMap(int width, int height) {
			this.width = width > 0 ? width : 1;
			this.height = height > 0 ? height : 1;
		}

		public int getWidth() {
			return width;
		}

		public void setWidth(int width) {
			this.width = width;
		}

		public int getHeight() {
			return height;
		}

		public void setHeight(int height) {
			this.height = height;
		}
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	
	public RecMap getOkMap() {
		return okMap;
	}

	
	public void setOkMap(RecMap okMap) {
		this.okMap = okMap;
	}

	
	public RecMap getPassMap() {
		return passMap;
	}

	
	public void setPassMap(RecMap passMap) {
		this.passMap = passMap;
	}

	
	public String getName() {
		return name;
	}

	
	public void setName(String name) {
		this.name = name;
	}

	
	public double getThreshold() {
		return threshold;
	}

	
	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	
	public Skeleton getCurrentSk() {
		return currentSk;
	}

	
	public void setCurrentSk(Skeleton currentSk) {
		this.currentSk = currentSk;
	}
	
}
