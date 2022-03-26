import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PostWithCommentsComponent } from './post-with-comments.component';

describe('PostWithCommentsComponent', () => {
  let component: PostWithCommentsComponent;
  let fixture: ComponentFixture<PostWithCommentsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PostWithCommentsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PostWithCommentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
