import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { Observable } from 'rxjs';
import { Image } from 'src/app/core/models/Image';
import { Publication } from 'src/app/core/models/Publication';
import { PublicationWrapper } from 'src/app/core/models/PublicationWrapper';
import { HttpService } from 'src/app/core/services/http.service';
import { UserService } from 'src/app/core/services/user.service';

@Component({
  selector: 'app-create-publication',
  templateUrl: './create-publication.component.html',
  styleUrls: ['./create-publication.component.css']
})
export class CreatePublicationComponent implements OnInit {
  files: File[] | null = null;
  newPublication : Publication = new Publication();
  confirmPassword : string;

  constructor(public http : HttpService, public toastr : ToastrService, public user : UserService) { }

  ngOnInit(): void {
  }

  handleFileInput(files: FileList) {
    if(this.files == null){
      this.files = [];
    }
    this.files.push(files.item(0));
  }

  removeFile(index : number){
    this.files.splice(index,1);
  }

  createPublication(){
    if(this.files == null){
      this.toastr.warning('Debe agregar fotos a la publicación','Advertencia');
    }

    let images = [];
    let promises = [];
    for (let file of this.files) {
        let filePromise = new Promise(resolve => {
            let reader = new FileReader();
            reader.readAsDataURL(file);
            // reader.readAsText(file);
            reader.onload = () => resolve(reader.result.toString());
        });
        promises.push(filePromise);
    }
    Promise.all(promises).then(fileContents => {
        // fileContents will be an array containing
        // the contents of the files, perform the
        // character replacements and other transformations
        // here as needed
        fileContents.forEach(data => {
          let image = new Image;
          image.imageBytes = data;
          images.push(image)
        });

        this.newPublication.images = images;
        this.newPublication.accountEmail = this.user.userSubject.getValue().email;
        
        var publicationWrapper = new PublicationWrapper();
        publicationWrapper.images = images;
        publicationWrapper.publication = this.newPublication;
        this.http.CreatePublication(publicationWrapper).subscribe(data => {
          this.newPublication.idPublication = data;
        });
    });
  }

}
