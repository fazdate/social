// This file can be replaced during build by using the `fileReplacements` array.
// `ng build` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  firebase: {
    projectId: 'social-2ccfd',
    appId: '1:945868348717:web:8bcde264442e34554f47a4',
    storageBucket: 'social-2ccfd.appspot.com',
    locationId: 'europe-west',
    apiKey: 'AIzaSyDCwJv4o25RH6UFKdnwsHSaRqK71pTmNkE',
    authDomain: 'social-2ccfd.firebaseapp.com',
    messagingSenderId: '945868348717',
  },
  production: false
};

export const api = 'http://localhost:8080/'

export const urls = {
  createUserurl: api + 'createUser',
  addCommentUrl: api + 'addComment',
  generateCommentIdUrl: api + 'generateCommentId',
  getCommentUrl: api + 'getComment',
  deleteCommentUrl: api + 'deleteComment',
  getEveryCommentOfPostUrl: api + 'getEveryCommentOfPost',
  getCommentWithUserData: api + 'getCommentWithUserData',
  getMessageUrl: api + 'getMessage',
  getMessagesListUrl: api + 'getMessagesListId',
  getEveryMessageFromMessagesListUrl: api + 'getEveryMessageFromMessagesList',
  sendMessageUrl: api + 'sendMessage',
  generateMessageIdUrl: api + 'generateMessageId',
  getMessagesListWIthUserDataUrl: api + 'getMessagesListWIthUserData',
  getUsersEveryMessagesListWithUserDatUrl: api + 'getUsersEveryMessagesListWithUserData',
  getPostUserCommentsArrayUrl: api + 'getPostUserCommentsArray',
  getOwnPostUserCommentsUrl: api + 'getOwnPostUserComments',
  getFollowedUsersPostUserComments: api + 'getFollowedUsersPostUserComments',
  getPostUserComment: api + 'getPostUserComment',
  createPostUrl: api + 'createPost',
  generatePostId: api + 'generatePostId',
  likeOrUnlikePostUrl: api + 'likeOrUnlikePost',
  updateUserUrl: api + 'updateUser',
  getUserUrl: api + 'getUser',
  followOrUnfollowUserUrl: api + 'followOrUnfollowUser',
  getEveryUsernameUrl: api + 'getEveryUsername',
  getDisplayNameFromUsernameUrl: api + 'getDisplayNameFromUsername'
}



/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/plugins/zone-error';  // Included with Angular CLI.
