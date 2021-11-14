import { Component, OnInit, Input,Output, EventEmitter, HostListener,SimpleChanges} from "@angular/core";

interface SelectorObject{
  id : number;
  description : string;
}

@Component({
  selector: "app-selector",
  templateUrl: "./selector.component.html",
  styleUrls : ["./selector.component.css"],
})
export class SelectorComponent implements OnInit {
  _options : any[];
  @Input() set options(value :any[] ){
    this._options = value;
    if(this._options){
      this.selectorOptions = this._options.map(opt => this.mapToInterface(opt));
    }    
  }
  @Input() keepDefaultText:boolean = false;
  @Input() placeholder : string;
  @Output() selectorItemReturn = new EventEmitter();

  selectorOptions : SelectorObject[];
  currentOptions : SelectorObject[];
  selectedElement : SelectorObject | undefined;

  selectorOn:boolean;
  hoverI:number=0;

  constructor() {}

  ngOnInit(): void {
    this.selectorOn = false;
    if(this._options){
      this.selectorOptions = this._options.map(opt => this.mapToInterface(opt));
      this.currentOptions = this.selectorOptions;
    }
    
  }

  setOptions(options : any[]){
    if(options)
    this.selectorOptions = options.map(opt => this.mapToInterface(opt));
    this.currentOptions = this.selectorOptions;
    this._options = this.selectorOptions;
  }

  mapToInterface(option : any) : SelectorObject{
    if(option){
      if(option.documentCi){ //If the option is a driver
        return {id: option.idDriver, description: option.driverName+" "+option.driverLastName};
      }
      if(option.licensePlate){ //If the option is a truck
        if(option.idTruck)
        return {id: option.idTruck, description: option.licensePlate};
        return {id: option.idTrailer, description: option.licensePlate};
      }
      if(option.name){ //If the option is a place
        if(option.idPlace)
        return {id: option.idPlace, description: option.name};
        return {id: option.idCompany, description: option.name}
      }
      if(option.description){
        return {id: option.idRestriction, description: option.description};
      }
      if(option.idWoodType){//If the option is a wood Type
        return {id: option.idWoodType, description: option.woodTypeName };
      }
      if(option.idLogSize){ //If the option is a log size
        return {id: option.idLogSize, description: option.logSize};
      }
    }
    return {id: 1, description: option};
  }

  selectOption(event:any,option:SelectorObject){
    this.toggleSelector(event);
    if(!this.keepDefaultText){
      this.selectedElement = option;
    }

    let index = this.selectorOptions.indexOf(option);
    this.selectorItemReturn.emit(this._options[index]);
  }

  toggleSelector(event:Event){
    event.stopPropagation();
    this.selectorOn = !this.selectorOn;
  }

  enterOption(id:number){
    this.hoverI = id;
  }

  enterKeyDownOption(event:any,id:number)
  {
    event.preventDefault();
    if(id<this._options.length-1){
      this.hoverI = id+1;
    }
  }

  enterKeyUpOption(event:any,id:number)
  {
    event.preventDefault();
    if(id>0){
    this.hoverI = id-1;
    }
  }

  selectOptionWithEnter(){
    if(!this.keepDefaultText){
      this.selectedElement = this._options[this.hoverI];
    }
    this.selectorItemReturn.emit(this.selectedElement);
  }

  @HostListener('document:click', ['$event']) onDocumentClick(event:any) {
    this.selectorOn = false;
  }
  reset(){
    this.selectedElement = undefined;
  }

  assignSelectedElement(element : any){
    let opt = this.mapToInterface(element);
    this.selectedElement = opt;
  }

  closeSelector(){
    this.selectorOn = false;
  }

  onFilter(event : any){
    event.stopPropagation();
    let description = event.target.value.toLowerCase();
    if (description != ''){
      this.currentOptions = this.selectorOptions.filter(function (d) {
        return d.description.toLowerCase().indexOf(description) != -1 || !description;
      });
    } else {
      this.currentOptions = this.selectorOptions;
    }
  }
}
