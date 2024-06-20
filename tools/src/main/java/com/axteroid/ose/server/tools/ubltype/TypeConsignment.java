package com.axteroid.ose.server.tools.ubltype;

import java.util.List;

public class TypeConsignment {
    protected String id;
    protected String carrierAssignedID;
    protected String consigneeAssignedID;
    protected String consignorAssignedID;
    protected String freightForwarderAssignedID;
    protected String brokerAssignedID;
    protected String contractedCarrierAssignedID;
    protected String performingCarrierAssignedID;
    protected List<String> summaryDescription;
    protected String totalInvoiceAmount;
    protected String declaredCustomsValueAmount;
    protected List<String> tariffDescription;
    protected String tariffCode;
    protected String insurancePremiumAmount;
    protected String grossWeightMeasure;
    protected String netWeightMeasure;
    protected String netNetWeightMeasure;
    protected String chargeableWeightMeasure;
    protected String grossVolumeMeasure;
    protected String netVolumeMeasure;
    protected String loadingLengthMeasure;
    protected List<String> remarks;
    protected String hazardousRiskIndicator;
    protected String animalFoodIndicator;
    protected String humanFoodIndicator;
    protected String livestockIndicator;
    protected String bulkCargoIndicator;
    protected String containerizedIndicator;
    protected String generalCargoIndicator;
    protected String specialSecurityIndicator;
    protected String thirdPartyPayerIndicator;
    protected List<String> carrierServiceInstructions;
    protected List<String> customsClearanceServiceInstructions;
    protected List<String> forwarderServiceInstructions;
    protected List<String> specialServiceInstructions;
    protected String sequenceID;
    protected String shippingPriorityLevelCode;
    protected String handlingCode;
    protected List<String> handlingInstructions;
    protected List<String> information;
    protected String totalGoodsItemQuantity;
    protected String totalTransportHandlingUnitQuantity;
    protected String insuranceValueAmount;
    protected String declaredForCarriageValueAmount;
    protected String declaredStatisticsValueAmount;
    protected String freeOnBoardValueAmount;
    protected List<String> specialInstructions;
    protected String splitConsignmentIndicator;
    protected List<String> deliveryInstructions;
    protected String consignmentQuantity;
    protected String consolidatableIndicator;
    protected List<String> haulageInstructions;
    protected String loadingSequenceID;
    protected String childConsignmentQuantity;
    protected String totalPackagesQuantity;
    protected List<String> consolidatedShipment;
    protected List<String> customsDeclaration;
    protected TypeTransportEvent requestedPickupTransportEvent;
    protected TypeTransportEvent requestedDeliveryTransportEvent;
    protected TypeTransportEvent plannedPickupTransportEvent;
    protected TypeTransportEvent plannedDeliveryTransportEvent;
    protected List<String> status;
    protected List<TypeConsignment> childConsignment;
    protected TypeParty consigneeParty;
    protected TypeParty exporterParty;
    protected TypeParty consignorParty;
    protected TypeParty importerParty;
    protected TypeParty carrierParty;
    protected TypeParty freightForwarderParty;
    protected TypeParty notifyParty;
    protected TypeParty originalDespatchParty;
    protected TypeParty finalDeliveryParty;
    protected TypeParty performingCarrierParty;
    protected TypeParty substituteCarrierParty;
    protected TypeParty logisticsOperatorParty;
    protected TypeParty transportAdvisorParty;
    protected TypeParty hazardousItemNotificationParty;
    protected TypeParty insuranceParty;
    protected TypeParty mortgageHolderParty;
    protected TypeParty billOfLadingHolderParty;
    protected String originalDepartureCountry;
    protected String finalDestinationCountry;
    protected List<String> transitCountry;
    protected String transportContract;
    protected List<String> transportEvent;
    protected String originalDespatchTransportationService;
    protected String finalDeliveryTransportationService;
    protected String deliveryTerms;
    protected String paymentTerms;
    protected String collectPaymentTerms;
    protected String disbursementPaymentTerms;
    protected String prepaidPaymentTerms;
    protected List<String> freightAllowanceCharge;
    protected List<String> extraAllowanceCharge;
    protected List<String> mainCarriageShipmentStage;
    protected List<String> preCarriageShipmentStage;
    protected List<String> onCarriageShipmentStage;
    protected List<String> transportHandlingUnit;
    protected TypeLocation firstArrivalPortLocation;
    protected TypeLocation lastExitPortLocation;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCarrierAssignedID() {
		return carrierAssignedID;
	}
	public void setCarrierAssignedID(String carrierAssignedID) {
		this.carrierAssignedID = carrierAssignedID;
	}
	public String getConsigneeAssignedID() {
		return consigneeAssignedID;
	}
	public void setConsigneeAssignedID(String consigneeAssignedID) {
		this.consigneeAssignedID = consigneeAssignedID;
	}
	public String getConsignorAssignedID() {
		return consignorAssignedID;
	}
	public void setConsignorAssignedID(String consignorAssignedID) {
		this.consignorAssignedID = consignorAssignedID;
	}
	public String getFreightForwarderAssignedID() {
		return freightForwarderAssignedID;
	}
	public void setFreightForwarderAssignedID(String freightForwarderAssignedID) {
		this.freightForwarderAssignedID = freightForwarderAssignedID;
	}
	public String getBrokerAssignedID() {
		return brokerAssignedID;
	}
	public void setBrokerAssignedID(String brokerAssignedID) {
		this.brokerAssignedID = brokerAssignedID;
	}
	public String getContractedCarrierAssignedID() {
		return contractedCarrierAssignedID;
	}
	public void setContractedCarrierAssignedID(String contractedCarrierAssignedID) {
		this.contractedCarrierAssignedID = contractedCarrierAssignedID;
	}
	public String getPerformingCarrierAssignedID() {
		return performingCarrierAssignedID;
	}
	public void setPerformingCarrierAssignedID(String performingCarrierAssignedID) {
		this.performingCarrierAssignedID = performingCarrierAssignedID;
	}
	public List<String> getSummaryDescription() {
		return summaryDescription;
	}
	public void setSummaryDescription(List<String> summaryDescription) {
		this.summaryDescription = summaryDescription;
	}
	public String getTotalInvoiceAmount() {
		return totalInvoiceAmount;
	}
	public void setTotalInvoiceAmount(String totalInvoiceAmount) {
		this.totalInvoiceAmount = totalInvoiceAmount;
	}
	public String getDeclaredCustomsValueAmount() {
		return declaredCustomsValueAmount;
	}
	public void setDeclaredCustomsValueAmount(String declaredCustomsValueAmount) {
		this.declaredCustomsValueAmount = declaredCustomsValueAmount;
	}
	public List<String> getTariffDescription() {
		return tariffDescription;
	}
	public void setTariffDescription(List<String> tariffDescription) {
		this.tariffDescription = tariffDescription;
	}
	public String getTariffCode() {
		return tariffCode;
	}
	public void setTariffCode(String tariffCode) {
		this.tariffCode = tariffCode;
	}
	public String getInsurancePremiumAmount() {
		return insurancePremiumAmount;
	}
	public void setInsurancePremiumAmount(String insurancePremiumAmount) {
		this.insurancePremiumAmount = insurancePremiumAmount;
	}
	public String getGrossWeightMeasure() {
		return grossWeightMeasure;
	}
	public void setGrossWeightMeasure(String grossWeightMeasure) {
		this.grossWeightMeasure = grossWeightMeasure;
	}
	public String getNetWeightMeasure() {
		return netWeightMeasure;
	}
	public void setNetWeightMeasure(String netWeightMeasure) {
		this.netWeightMeasure = netWeightMeasure;
	}
	public String getNetNetWeightMeasure() {
		return netNetWeightMeasure;
	}
	public void setNetNetWeightMeasure(String netNetWeightMeasure) {
		this.netNetWeightMeasure = netNetWeightMeasure;
	}
	public String getChargeableWeightMeasure() {
		return chargeableWeightMeasure;
	}
	public void setChargeableWeightMeasure(String chargeableWeightMeasure) {
		this.chargeableWeightMeasure = chargeableWeightMeasure;
	}
	public String getGrossVolumeMeasure() {
		return grossVolumeMeasure;
	}
	public void setGrossVolumeMeasure(String grossVolumeMeasure) {
		this.grossVolumeMeasure = grossVolumeMeasure;
	}
	public String getNetVolumeMeasure() {
		return netVolumeMeasure;
	}
	public void setNetVolumeMeasure(String netVolumeMeasure) {
		this.netVolumeMeasure = netVolumeMeasure;
	}
	public String getLoadingLengthMeasure() {
		return loadingLengthMeasure;
	}
	public void setLoadingLengthMeasure(String loadingLengthMeasure) {
		this.loadingLengthMeasure = loadingLengthMeasure;
	}
	public List<String> getRemarks() {
		return remarks;
	}
	public void setRemarks(List<String> remarks) {
		this.remarks = remarks;
	}
	public String getHazardousRiskIndicator() {
		return hazardousRiskIndicator;
	}
	public void setHazardousRiskIndicator(String hazardousRiskIndicator) {
		this.hazardousRiskIndicator = hazardousRiskIndicator;
	}
	public String getAnimalFoodIndicator() {
		return animalFoodIndicator;
	}
	public void setAnimalFoodIndicator(String animalFoodIndicator) {
		this.animalFoodIndicator = animalFoodIndicator;
	}
	public String getHumanFoodIndicator() {
		return humanFoodIndicator;
	}
	public void setHumanFoodIndicator(String humanFoodIndicator) {
		this.humanFoodIndicator = humanFoodIndicator;
	}
	public String getLivestockIndicator() {
		return livestockIndicator;
	}
	public void setLivestockIndicator(String livestockIndicator) {
		this.livestockIndicator = livestockIndicator;
	}
	public String getBulkCargoIndicator() {
		return bulkCargoIndicator;
	}
	public void setBulkCargoIndicator(String bulkCargoIndicator) {
		this.bulkCargoIndicator = bulkCargoIndicator;
	}
	public String getContainerizedIndicator() {
		return containerizedIndicator;
	}
	public void setContainerizedIndicator(String containerizedIndicator) {
		this.containerizedIndicator = containerizedIndicator;
	}
	public String getGeneralCargoIndicator() {
		return generalCargoIndicator;
	}
	public void setGeneralCargoIndicator(String generalCargoIndicator) {
		this.generalCargoIndicator = generalCargoIndicator;
	}
	public String getSpecialSecurityIndicator() {
		return specialSecurityIndicator;
	}
	public void setSpecialSecurityIndicator(String specialSecurityIndicator) {
		this.specialSecurityIndicator = specialSecurityIndicator;
	}
	public String getThirdPartyPayerIndicator() {
		return thirdPartyPayerIndicator;
	}
	public void setThirdPartyPayerIndicator(String thirdPartyPayerIndicator) {
		this.thirdPartyPayerIndicator = thirdPartyPayerIndicator;
	}
	public List<String> getCarrierServiceInstructions() {
		return carrierServiceInstructions;
	}
	public void setCarrierServiceInstructions(List<String> carrierServiceInstructions) {
		this.carrierServiceInstructions = carrierServiceInstructions;
	}
	public List<String> getCustomsClearanceServiceInstructions() {
		return customsClearanceServiceInstructions;
	}
	public void setCustomsClearanceServiceInstructions(List<String> customsClearanceServiceInstructions) {
		this.customsClearanceServiceInstructions = customsClearanceServiceInstructions;
	}
	public List<String> getForwarderServiceInstructions() {
		return forwarderServiceInstructions;
	}
	public void setForwarderServiceInstructions(List<String> forwarderServiceInstructions) {
		this.forwarderServiceInstructions = forwarderServiceInstructions;
	}
	public List<String> getSpecialServiceInstructions() {
		return specialServiceInstructions;
	}
	public void setSpecialServiceInstructions(List<String> specialServiceInstructions) {
		this.specialServiceInstructions = specialServiceInstructions;
	}
	public String getSequenceID() {
		return sequenceID;
	}
	public void setSequenceID(String sequenceID) {
		this.sequenceID = sequenceID;
	}
	public String getShippingPriorityLevelCode() {
		return shippingPriorityLevelCode;
	}
	public void setShippingPriorityLevelCode(String shippingPriorityLevelCode) {
		this.shippingPriorityLevelCode = shippingPriorityLevelCode;
	}
	public String getHandlingCode() {
		return handlingCode;
	}
	public void setHandlingCode(String handlingCode) {
		this.handlingCode = handlingCode;
	}
	public List<String> getHandlingInstructions() {
		return handlingInstructions;
	}
	public void setHandlingInstructions(List<String> handlingInstructions) {
		this.handlingInstructions = handlingInstructions;
	}
	public List<String> getInformation() {
		return information;
	}
	public void setInformation(List<String> information) {
		this.information = information;
	}
	public String getTotalGoodsItemQuantity() {
		return totalGoodsItemQuantity;
	}
	public void setTotalGoodsItemQuantity(String totalGoodsItemQuantity) {
		this.totalGoodsItemQuantity = totalGoodsItemQuantity;
	}
	public String getTotalTransportHandlingUnitQuantity() {
		return totalTransportHandlingUnitQuantity;
	}
	public void setTotalTransportHandlingUnitQuantity(String totalTransportHandlingUnitQuantity) {
		this.totalTransportHandlingUnitQuantity = totalTransportHandlingUnitQuantity;
	}
	public String getInsuranceValueAmount() {
		return insuranceValueAmount;
	}
	public void setInsuranceValueAmount(String insuranceValueAmount) {
		this.insuranceValueAmount = insuranceValueAmount;
	}
	public String getDeclaredForCarriageValueAmount() {
		return declaredForCarriageValueAmount;
	}
	public void setDeclaredForCarriageValueAmount(String declaredForCarriageValueAmount) {
		this.declaredForCarriageValueAmount = declaredForCarriageValueAmount;
	}
	public String getDeclaredStatisticsValueAmount() {
		return declaredStatisticsValueAmount;
	}
	public void setDeclaredStatisticsValueAmount(String declaredStatisticsValueAmount) {
		this.declaredStatisticsValueAmount = declaredStatisticsValueAmount;
	}
	public String getFreeOnBoardValueAmount() {
		return freeOnBoardValueAmount;
	}
	public void setFreeOnBoardValueAmount(String freeOnBoardValueAmount) {
		this.freeOnBoardValueAmount = freeOnBoardValueAmount;
	}
	public List<String> getSpecialInstructions() {
		return specialInstructions;
	}
	public void setSpecialInstructions(List<String> specialInstructions) {
		this.specialInstructions = specialInstructions;
	}
	public String getSplitConsignmentIndicator() {
		return splitConsignmentIndicator;
	}
	public void setSplitConsignmentIndicator(String splitConsignmentIndicator) {
		this.splitConsignmentIndicator = splitConsignmentIndicator;
	}
	public List<String> getDeliveryInstructions() {
		return deliveryInstructions;
	}
	public void setDeliveryInstructions(List<String> deliveryInstructions) {
		this.deliveryInstructions = deliveryInstructions;
	}
	public String getConsignmentQuantity() {
		return consignmentQuantity;
	}
	public void setConsignmentQuantity(String consignmentQuantity) {
		this.consignmentQuantity = consignmentQuantity;
	}
	public String getConsolidatableIndicator() {
		return consolidatableIndicator;
	}
	public void setConsolidatableIndicator(String consolidatableIndicator) {
		this.consolidatableIndicator = consolidatableIndicator;
	}
	public List<String> getHaulageInstructions() {
		return haulageInstructions;
	}
	public void setHaulageInstructions(List<String> haulageInstructions) {
		this.haulageInstructions = haulageInstructions;
	}
	public String getLoadingSequenceID() {
		return loadingSequenceID;
	}
	public void setLoadingSequenceID(String loadingSequenceID) {
		this.loadingSequenceID = loadingSequenceID;
	}
	public String getChildConsignmentQuantity() {
		return childConsignmentQuantity;
	}
	public void setChildConsignmentQuantity(String childConsignmentQuantity) {
		this.childConsignmentQuantity = childConsignmentQuantity;
	}
	public String getTotalPackagesQuantity() {
		return totalPackagesQuantity;
	}
	public void setTotalPackagesQuantity(String totalPackagesQuantity) {
		this.totalPackagesQuantity = totalPackagesQuantity;
	}
	public List<String> getConsolidatedShipment() {
		return consolidatedShipment;
	}
	public void setConsolidatedShipment(List<String> consolidatedShipment) {
		this.consolidatedShipment = consolidatedShipment;
	}
	public List<String> getCustomsDeclaration() {
		return customsDeclaration;
	}
	public void setCustomsDeclaration(List<String> customsDeclaration) {
		this.customsDeclaration = customsDeclaration;
	}
	public TypeTransportEvent getRequestedPickupTransportEvent() {
		return requestedPickupTransportEvent;
	}
	public void setRequestedPickupTransportEvent(TypeTransportEvent requestedPickupTransportEvent) {
		this.requestedPickupTransportEvent = requestedPickupTransportEvent;
	}
	public TypeTransportEvent getRequestedDeliveryTransportEvent() {
		return requestedDeliveryTransportEvent;
	}
	public void setRequestedDeliveryTransportEvent(TypeTransportEvent requestedDeliveryTransportEvent) {
		this.requestedDeliveryTransportEvent = requestedDeliveryTransportEvent;
	}
	public TypeTransportEvent getPlannedPickupTransportEvent() {
		return plannedPickupTransportEvent;
	}
	public void setPlannedPickupTransportEvent(TypeTransportEvent plannedPickupTransportEvent) {
		this.plannedPickupTransportEvent = plannedPickupTransportEvent;
	}
	public TypeTransportEvent getPlannedDeliveryTransportEvent() {
		return plannedDeliveryTransportEvent;
	}
	public void setPlannedDeliveryTransportEvent(TypeTransportEvent plannedDeliveryTransportEvent) {
		this.plannedDeliveryTransportEvent = plannedDeliveryTransportEvent;
	}
	public List<String> getStatus() {
		return status;
	}
	public void setStatus(List<String> status) {
		this.status = status;
	}
	public List<TypeConsignment> getChildConsignment() {
		return childConsignment;
	}
	public void setChildConsignment(List<TypeConsignment> childConsignment) {
		this.childConsignment = childConsignment;
	}
	public TypeParty getConsigneeParty() {
		return consigneeParty;
	}
	public void setConsigneeParty(TypeParty consigneeParty) {
		this.consigneeParty = consigneeParty;
	}
	public TypeParty getExporterParty() {
		return exporterParty;
	}
	public void setExporterParty(TypeParty exporterParty) {
		this.exporterParty = exporterParty;
	}
	public TypeParty getConsignorParty() {
		return consignorParty;
	}
	public void setConsignorParty(TypeParty consignorParty) {
		this.consignorParty = consignorParty;
	}
	public TypeParty getImporterParty() {
		return importerParty;
	}
	public void setImporterParty(TypeParty importerParty) {
		this.importerParty = importerParty;
	}
	public TypeParty getCarrierParty() {
		return carrierParty;
	}
	public void setCarrierParty(TypeParty carrierParty) {
		this.carrierParty = carrierParty;
	}
	public TypeParty getFreightForwarderParty() {
		return freightForwarderParty;
	}
	public void setFreightForwarderParty(TypeParty freightForwarderParty) {
		this.freightForwarderParty = freightForwarderParty;
	}
	public TypeParty getNotifyParty() {
		return notifyParty;
	}
	public void setNotifyParty(TypeParty notifyParty) {
		this.notifyParty = notifyParty;
	}
	public TypeParty getOriginalDespatchParty() {
		return originalDespatchParty;
	}
	public void setOriginalDespatchParty(TypeParty originalDespatchParty) {
		this.originalDespatchParty = originalDespatchParty;
	}
	public TypeParty getFinalDeliveryParty() {
		return finalDeliveryParty;
	}
	public void setFinalDeliveryParty(TypeParty finalDeliveryParty) {
		this.finalDeliveryParty = finalDeliveryParty;
	}
	public TypeParty getPerformingCarrierParty() {
		return performingCarrierParty;
	}
	public void setPerformingCarrierParty(TypeParty performingCarrierParty) {
		this.performingCarrierParty = performingCarrierParty;
	}
	public TypeParty getSubstituteCarrierParty() {
		return substituteCarrierParty;
	}
	public void setSubstituteCarrierParty(TypeParty substituteCarrierParty) {
		this.substituteCarrierParty = substituteCarrierParty;
	}
	public TypeParty getLogisticsOperatorParty() {
		return logisticsOperatorParty;
	}
	public void setLogisticsOperatorParty(TypeParty logisticsOperatorParty) {
		this.logisticsOperatorParty = logisticsOperatorParty;
	}
	public TypeParty getTransportAdvisorParty() {
		return transportAdvisorParty;
	}
	public void setTransportAdvisorParty(TypeParty transportAdvisorParty) {
		this.transportAdvisorParty = transportAdvisorParty;
	}
	public TypeParty getHazardousItemNotificationParty() {
		return hazardousItemNotificationParty;
	}
	public void setHazardousItemNotificationParty(TypeParty hazardousItemNotificationParty) {
		this.hazardousItemNotificationParty = hazardousItemNotificationParty;
	}
	public TypeParty getInsuranceParty() {
		return insuranceParty;
	}
	public void setInsuranceParty(TypeParty insuranceParty) {
		this.insuranceParty = insuranceParty;
	}
	public TypeParty getMortgageHolderParty() {
		return mortgageHolderParty;
	}
	public void setMortgageHolderParty(TypeParty mortgageHolderParty) {
		this.mortgageHolderParty = mortgageHolderParty;
	}
	public TypeParty getBillOfLadingHolderParty() {
		return billOfLadingHolderParty;
	}
	public void setBillOfLadingHolderParty(TypeParty billOfLadingHolderParty) {
		this.billOfLadingHolderParty = billOfLadingHolderParty;
	}
	public String getOriginalDepartureCountry() {
		return originalDepartureCountry;
	}
	public void setOriginalDepartureCountry(String originalDepartureCountry) {
		this.originalDepartureCountry = originalDepartureCountry;
	}
	public String getFinalDestinationCountry() {
		return finalDestinationCountry;
	}
	public void setFinalDestinationCountry(String finalDestinationCountry) {
		this.finalDestinationCountry = finalDestinationCountry;
	}
	public List<String> getTransitCountry() {
		return transitCountry;
	}
	public void setTransitCountry(List<String> transitCountry) {
		this.transitCountry = transitCountry;
	}
	public String getTransportContract() {
		return transportContract;
	}
	public void setTransportContract(String transportContract) {
		this.transportContract = transportContract;
	}
	public List<String> getTransportEvent() {
		return transportEvent;
	}
	public void setTransportEvent(List<String> transportEvent) {
		this.transportEvent = transportEvent;
	}
	public String getOriginalDespatchTransportationService() {
		return originalDespatchTransportationService;
	}
	public void setOriginalDespatchTransportationService(String originalDespatchTransportationService) {
		this.originalDespatchTransportationService = originalDespatchTransportationService;
	}
	public String getFinalDeliveryTransportationService() {
		return finalDeliveryTransportationService;
	}
	public void setFinalDeliveryTransportationService(String finalDeliveryTransportationService) {
		this.finalDeliveryTransportationService = finalDeliveryTransportationService;
	}
	public String getDeliveryTerms() {
		return deliveryTerms;
	}
	public void setDeliveryTerms(String deliveryTerms) {
		this.deliveryTerms = deliveryTerms;
	}
	public String getPaymentTerms() {
		return paymentTerms;
	}
	public void setPaymentTerms(String paymentTerms) {
		this.paymentTerms = paymentTerms;
	}
	public String getCollectPaymentTerms() {
		return collectPaymentTerms;
	}
	public void setCollectPaymentTerms(String collectPaymentTerms) {
		this.collectPaymentTerms = collectPaymentTerms;
	}
	public String getDisbursementPaymentTerms() {
		return disbursementPaymentTerms;
	}
	public void setDisbursementPaymentTerms(String disbursementPaymentTerms) {
		this.disbursementPaymentTerms = disbursementPaymentTerms;
	}
	public String getPrepaidPaymentTerms() {
		return prepaidPaymentTerms;
	}
	public void setPrepaidPaymentTerms(String prepaidPaymentTerms) {
		this.prepaidPaymentTerms = prepaidPaymentTerms;
	}
	public List<String> getFreightAllowanceCharge() {
		return freightAllowanceCharge;
	}
	public void setFreightAllowanceCharge(List<String> freightAllowanceCharge) {
		this.freightAllowanceCharge = freightAllowanceCharge;
	}
	public List<String> getExtraAllowanceCharge() {
		return extraAllowanceCharge;
	}
	public void setExtraAllowanceCharge(List<String> extraAllowanceCharge) {
		this.extraAllowanceCharge = extraAllowanceCharge;
	}
	public List<String> getMainCarriageShipmentStage() {
		return mainCarriageShipmentStage;
	}
	public void setMainCarriageShipmentStage(List<String> mainCarriageShipmentStage) {
		this.mainCarriageShipmentStage = mainCarriageShipmentStage;
	}
	public List<String> getPreCarriageShipmentStage() {
		return preCarriageShipmentStage;
	}
	public void setPreCarriageShipmentStage(List<String> preCarriageShipmentStage) {
		this.preCarriageShipmentStage = preCarriageShipmentStage;
	}
	public List<String> getOnCarriageShipmentStage() {
		return onCarriageShipmentStage;
	}
	public void setOnCarriageShipmentStage(List<String> onCarriageShipmentStage) {
		this.onCarriageShipmentStage = onCarriageShipmentStage;
	}
	public List<String> getTransportHandlingUnit() {
		return transportHandlingUnit;
	}
	public void setTransportHandlingUnit(List<String> transportHandlingUnit) {
		this.transportHandlingUnit = transportHandlingUnit;
	}
	public TypeLocation getFirstArrivalPortLocation() {
		return firstArrivalPortLocation;
	}
	public void setFirstArrivalPortLocation(TypeLocation firstArrivalPortLocation) {
		this.firstArrivalPortLocation = firstArrivalPortLocation;
	}
	public TypeLocation getLastExitPortLocation() {
		return lastExitPortLocation;
	}
	public void setLastExitPortLocation(TypeLocation lastExitPortLocation) {
		this.lastExitPortLocation = lastExitPortLocation;
	}
}
