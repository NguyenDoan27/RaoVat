package com.example.raovat_app.classes


class Address {
    private var idAddress: Int? = null
    private var name: String? = null
    private var phone: String? = null
    private var city: String? = null
    private var district: String? = null
    private var ward: String? = null
    private var village: String? = null
    private var nameKind: String? = null

    constructor( name: String?, phone: String?, city: String?, district: String?, ward: String?, village: String?, nameKind: String?){
        this.name = name
        this.phone = phone
        this.city = city
        this.district = district
        this.ward = ward
        this.village = village
        this.nameKind = nameKind
    }

    constructor()

    fun getIdAddress(): Int?{
        return idAddress
    }
    fun getName(): String?{
        return name
    }
    fun getPhone(): String?{
        return phone
    }
    fun getCity(): String?{
        return city
    }
    fun getDistrict(): String?{
        return district
    }
    fun getWard(): String?{
        return ward
    }
    fun getVillage(): String?{
        return village
    }
    fun  getNameKind(): String?{
        return nameKind

    }

}