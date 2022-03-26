import { TestBed } from '@angular/core/testing';

import { MessagesListWithUserDataService } from './messages-list-with-user-data.service';

describe('MessagesListWithUserDataService', () => {
  let service: MessagesListWithUserDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MessagesListWithUserDataService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
