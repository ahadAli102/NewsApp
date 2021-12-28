package com.example.newsapp.data;

import com.example.newsapp.model.FilterData;

import java.util.ArrayList;
import java.util.List;

public class FilterDataProvider {
    private static List<FilterData> mCountries;
    private static List<FilterData> mLanguages;
    private static List<FilterData> mCategories;


    public static List<FilterData> getFilterCountries(){
        if (mCountries == null){
            mCountries = getCountries();
        }
        return mCountries;
    }

    public static List<FilterData> getFilterLanguages(){
        if(mLanguages == null){
            mLanguages = getLanguages();
        }
        return mLanguages;

    }

    public static List<FilterData> getFilterCategories(){
        if (mCategories == null){
            mCategories = getCategories();
        }
        return mCategories;
    }

    private static List<FilterData> getCountries(){
        List<FilterData> data = new ArrayList<>();
        data.add(new FilterData("Argentina" , "ar" , false));
        data.add(new FilterData("Australia" , "au" , false));
        data.add(new FilterData("Austria" , "at" , false));
        data.add(new FilterData("Belgium" , "be" , false));
        data.add(new FilterData("Brazil" , "br" , false));
        data.add(new FilterData("Canada" , "ca" , false));
        data.add(new FilterData("China" , "cn" , false));
        data.add(new FilterData("Colombia" , "co" , false));
        data.add(new FilterData("Cuba" , "cu" , false));
        data.add(new FilterData("Czech republic" , "cz" , false));
        data.add(new FilterData("France" , "fr" , false));
        data.add(new FilterData("Germany" , "de" , false));
        data.add(new FilterData("Greece" , "gr" , false));
        data.add(new FilterData("Hong kong" , "hk" , false));
        data.add(new FilterData("Hungary" , "hu" , false));
        data.add(new FilterData("India" , "in" , false));
        data.add(new FilterData("Indonesia" , "id" , false));
        data.add(new FilterData("Ireland" , "ie" , false));
        data.add(new FilterData("Israel" , "il" , false));
        data.add(new FilterData("Italy" , "it" , false));
        data.add(new FilterData("Japan" , "jp" , false));
        data.add(new FilterData("Latvia" , "lv" , false));
        data.add(new FilterData("Lebanon" , "lb" , false));
        data.add(new FilterData("Lithuania" , "lt" , false));
        data.add(new FilterData("Malaysia" , "my" , false));
        data.add(new FilterData("Mexico" , "mx" , false));
        data.add(new FilterData("Morocco" , "ma" , false));
        data.add(new FilterData("Netherland" , "nl" , false));
        data.add(new FilterData("New zealand" , "nz" , false));
        data.add(new FilterData("Nigeria" , "ng" , false));
        data.add(new FilterData("North korea" , "kp" , false));
        data.add(new FilterData("Norway" , "no" , false));
        data.add(new FilterData("Pakistan" , "pk" , false));
        data.add(new FilterData("Philippines" , "ph" , false));
        data.add(new FilterData("Poland" , "pl" , false));
        data.add(new FilterData("Portugal" , "pt" , false));
        data.add(new FilterData("Romania" , "ro" , false));
        data.add(new FilterData("Russia" , "ru" , false));
        data.add(new FilterData("Saudi arabia" , "sa" , false));
        data.add(new FilterData("Serbia" , "rs" , false));
        data.add(new FilterData("Singapore" , "sg" , false));
        data.add(new FilterData("Slovakia" , "sk" , false));
        data.add(new FilterData("Slovenia" , "si" , false));
        data.add(new FilterData("South korea" , "kr" , false));
        data.add(new FilterData("Spain" , "es" , false));
        data.add(new FilterData("Sweden" , "se" , false));
        data.add(new FilterData("Switzerland" , "ch" , false));
        data.add(new FilterData("Taiwan" , "tw" , false));
        data.add(new FilterData("Thailand" , "th" , false));
        data.add(new FilterData("Turkey" , "tr" , false));
        data.add(new FilterData("Ukraine" , "ua" , false));
        data.add(new FilterData("United arab emirates" , "ae" , false));
        data.add(new FilterData("United kingdom" , "gb" , false));
        data.add(new FilterData("United states of america" , "us" , false));
        data.add(new FilterData("Venezuela" , "ve" , false));
        return data;
    }

    private static List<FilterData> getLanguages(){
        List<FilterData> data = new ArrayList<>();
        
        data.add(new FilterData("Arabic" , "ar" ,false));
        data.add(new FilterData("Bosnian" , "bs" ,false));
        data.add(new FilterData("Bulgarian" , "bg" ,false));
        data.add(new FilterData("Chinese" , "zh" ,false));
        data.add(new FilterData("Croatian" , "hr" ,false));
        data.add(new FilterData("Czech" , "cs" ,false));
        data.add(new FilterData("Dutch" , "nl" ,false));
        data.add(new FilterData("English" , "en" ,false));
        data.add(new FilterData("German" , "de" ,false));
        data.add(new FilterData("Greek" , "el" ,false));
        data.add(new FilterData("Hebrew" , "he" ,false));
        data.add(new FilterData("Hungarian" , "hu" ,false));
        data.add(new FilterData("Italian" , "it" ,false));
        data.add(new FilterData("Latvian" , "lv" ,false));
        data.add(new FilterData("Lithuanian" , "lt" ,false));
        data.add(new FilterData("Norwegian" , "no" ,false));
        data.add(new FilterData("Polish" , "pl" ,false));
        data.add(new FilterData("Portuguese" , "pt" ,false));
        data.add(new FilterData("Romanian" , "ro" ,false));
        data.add(new FilterData("Russian" , "ru" ,false));
        data.add(new FilterData("Serbian" , "sr" ,false));
        data.add(new FilterData("Slovak" , "sk" ,false));
        data.add(new FilterData("Slovenian" , "sl" ,false));
        data.add(new FilterData("Spanish" , "es" ,false));
        data.add(new FilterData("Swedish" , "sv" ,false));
        data.add(new FilterData("Thai" , "th" ,false));
        data.add(new FilterData("Turkish" , "tr" ,false));
        data.add(new FilterData("Ukrainian" , "uk" ,false));
        return data;
    }

    private static List<FilterData> getCategories(){
        List<FilterData> data = new ArrayList<>();
        data.add(new FilterData("business" , "business" , false));
        data.add(new FilterData("entertainment" , "entertainment" , false));
        data.add(new FilterData("environment" , "environment" , false));
        data.add(new FilterData("food" , "food" , false));
        data.add(new FilterData("politics" , "politics" , false));
        data.add(new FilterData("science" , "science" , false));
        data.add(new FilterData("sports" , "sports" , false));
        data.add(new FilterData("technology" , "technology" , false));
        data.add(new FilterData("top" , "top" , false));
        data.add(new FilterData("world" , "world" , false));

        return data;
    }
}
