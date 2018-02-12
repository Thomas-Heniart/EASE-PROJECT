import React, {Component} from "react";
import {connect} from "react-redux";
import TeamAddUserModal from '../teamModals/TeamAddUserModal';
import TeamAddChannelModal from '../teamModals/TeamAddChannelModal';
import TeamDeleteUserModal from '../teamModals/TeamDeleteUserModal';
import TeamDeleteRoomModal from '../teamModals/TeamDeleteRoomModal';
import TeamDeleteUserFromRoomModal from '../teamModals/TeamDeleteUserFromRoomModal';
import PinTeamAppToDashboardModal from '../teamModals/PinTeamAppToDashboardModal';
import TeamDeleteAppModal from '../teamModals/TeamDeleteAppModal';
import TeamLeaveAppModal from '../teamModals/TeamLeaveAppModal';
import TeamManageAppRequestModal from '../teamModals/TeamManageAppRequestModal';
import TeamTransferOwnershipModal from '../teamModals/TeamTransferOwnershipModal';
import TeamPhoneNumberModal from '../teamModals/TeamPhoneNumberModal';
import RequestWebsiteModal from '../teamModals/RequestWebsiteModal';
import AskJoinEnterpriseAppModal from "../teamModals/AskJoinEnterpriseAppModal";
import VerifyTeamUserModal from '../teamModals/VerifyTeamUserModal';
import ReactivateTeamUserModal from '../teamModals/ReactivateTeamUserModal';
import TeamAddMultipleUsersModal from '../teamModals/TeamAddMultipleUsersModal';
import JoinEnterpriseAppModal from "../teamModals/JoinEnterpriseAppModal";
import AcceptEnterpriseAppModal from "../teamModals/AcceptEnterpriseAppModal";
import EditEnterpriseAppModal from "../teamModals/EditEnterpriseAppModal";
import FreeTrialEndModal from "../teamModals/FreeTrialEndModal";
import UpgradeTeamPlanModal from "../teamModals/UpgradeTeamPlanModal";
import TeamUserInviteLimitReachedModal from "../teamModals/TeamUserInviteLimitReachedModal";
import DepartureDateEndModal from "../teamModals/DepartureDateEndModal";
import CatalogAddBookmarkModal from "../catalog/AddBookmarkModal";
import ClassicAppModal from "../catalog/ClassicAppModal";
import SsoAppModal from "../catalog/SsoAppModal";
import ClassicAppSettingsModal from "../modals/ClassicAppSettingsModal";
import ExtensionDownloadModal from "../modals/ExtensionDownloadModal";
import LinkAppSettingsModal from "../modals/LinkAppSettingsModal";
import AnyAppSettingsModal from "../modals/AnyAppSettingsModal";
import SoftwareAppSettingsModal from "../modals/SoftwareAppSettingsModal";
import TeamSingleAppSettingsModal from "../modals/TeamSingleAppSettingsModal";
import TeamAnySingleAppSettingsModal from "../modals/TeamAnySingleAppSettingsModal";
import TeamSoftwareSingleAppSettingsModal from "../modals/TeamSoftwareSingleAppSettingsModal";
import TeamEnterpriseAppSettingsModal from "../modals/TeamEnterpriseAppSettingsModal";
import TeamAnyEnterpriseAppSettingsModal from "../modals/TeamAnyEnterpriseAppSettingsModal";
import TeamSoftwareEnterpriseAppSettingsModal from "../modals/TeamSoftwareEnterpriseAppSettingsModal";
import TeamLinkAppSettingsModal from "../modals/TeamLinkAppSettingsModal";
import PasswordLostInformationModal from "../modals/PasswordLostInformationModal";
import LockedTeamAppModal from "../modals/LockedTeamAppModal";
import LogWithAppSettings from "../modals/LogWithAppSettings";
import SsoAppSettingsModal from "../modals/SsoAppSettingsModal";
import ChooseAppCredentialsModal from "../teamAppAdders/ChooseAppCredentialsModal";
import ChooseAnyAppCredentialsModal from "../teamAppAdders/ChooseAnyAppCredentialsModal";
import ChooseSoftwareAppCredentialsModal from "../teamAppAdders/ChooseSoftwareAppCredentialsModal";
import UpdateAppPasswordModal from "../modals/UpdateAppPasswordModal";
import NewFeatureModal from "../modals/NewFeatureModal";
import AddAnyAppModal from "../catalog/AddAnyAppModal";
import AddSoftwareCredentialsModal from "../catalog/AddSoftwareCredentialsModal";
import InviteTeamUsersModal from "../teamModals/InviteTeamUsersModal";
import SimpleAppFillerChooserModal from "../teamModals/SimpleAppFillerChooserModal";
import FillSimpleCardCredentialsModal from "../teamModals/FillSimpleCardCredentialsModal";
import AccountUpdateModal from "../catalog/Updates/AccountUpdateModal";

@connect(store => ({
  addUserModal: store.teamModals.addUserModal,
  addChannelModal: store.teamModals.addChannelModal,
  teamDeleteUserModal: store.teamModals.teamDeleteUserModal,
  teamDeleteChannelModal: store.teamModals.teamDeleteChannelModal,
  teamDeleteUserFromChannelModal: store.teamModals.teamDeleteUserFromChannelModal,
  teamDeleteAppModal: store.teamModals.teamDeleteAppModal,
  pinTeamAppToDashboardModal: store.teamModals.pinTeamAppToDashboardModal,
  teamLeaveAppModal: store.teamModals.teamLeaveAppModal,
  teamEditEnterpriseAppModal: store.teamModals.teamEditEnterpriseAppModal,
  teamManageAppRequestModal: store.teamModals.teamManageAppRequestModal,
  teamAcceptMultiAppModal: store.teamModals.teamAcceptMultiAppModal,
  teamJoinEnterpriseAppModal: store.teamModals.teamJoinEnterpriseAppModal,
  teamAskJoinEnterpriseAppModal: store.teamModals.teamAskJoinEnterpriseAppModal,
  teamSettingsModalActive: store.teamModals.teamSettingsModalActive,
  verifyTeamUserModal: store.teamModals.verifyTeamUserModal,
  teamAddMultipleUsersModal: store.teamModals.teamAddMultipleUsersModal,
  reactivateTeamUserModal: store.teamModals.reactivateTeamUserModal,
  teamTransferOwnershipModal: store.teamModals.teamTransferOwnershipModal,
  teamPhoneNumberModal: store.teamModals.teamPhoneNumberModal,
  requestWebsiteModal: store.teamModals.requestWebsiteModal,
  freeTrialEndModal: store.teamModals.freeTrialEndModal,
  upgradeTeamPlanModal: store.teamModals.upgradeTeamPlanModal,
  teamUserInviteLimitReachedModal: store.teamModals.teamUserInviteLimitReachedModal,
  departureDateEndModal: store.teamModals.departureDateEndModal,
  catalogAddBookmarkModal: store.teamModals.catalogAddBookmarkModal,
  catalogAddAppModal: store.teamModals.catalogAddAppModal,
  catalogAddSSOAppModal: store.teamModals.catalogAddSSOAppModal,
  catalogAddAnyAppModal: store.teamModals.catalogAddAnyAppModal,
  catalogAddSoftwareAppModal: store.teamModals.catalogAddSoftwareAppModal,
  inviteTeamUsersModal: store.teamModals.inviteTeamUsersModal,
  simpleAppFillerChooserModal: store.teamModals.simpleAppFillerChooserModal,
  fillSimpleCardCredentialsModal: store.teamModals.fillSimpleCardCredentialsModal,
  modals: store.modals
}))
class ModalsContainer extends Component{
  constructor(props){
    super(props);
  }
  render() {
    return (
        <div>
          {this.props.addUserModal.active &&
          <TeamAddUserModal/>}
          {this.props.addChannelModal.active &&
          <TeamAddChannelModal/>}
          {this.props.teamDeleteUserModal.active &&
          <TeamDeleteUserModal/>}
          {this.props.teamDeleteChannelModal.active &&
          <TeamDeleteRoomModal/>}
          {this.props.teamDeleteUserFromChannelModal.active &&
          <TeamDeleteUserFromRoomModal/>}
          {this.props.teamDeleteAppModal.active &&
          <TeamDeleteAppModal/>}
          {this.props.pinTeamAppToDashboardModal.active &&
          <PinTeamAppToDashboardModal/>}
          {this.props.teamLeaveAppModal.active &&
          <TeamLeaveAppModal/>}
          {this.props.teamEditEnterpriseAppModal.active &&
          <EditEnterpriseAppModal/>}
          {this.props.teamManageAppRequestModal.active &&
          <TeamManageAppRequestModal/>}
          {this.props.teamAcceptMultiAppModal.active &&
          <AcceptEnterpriseAppModal/>}
          {this.props.teamJoinEnterpriseAppModal.active &&
          <JoinEnterpriseAppModal/>}
          {this.props.teamAskJoinEnterpriseAppModal.active &&
          <AskJoinEnterpriseAppModal/>}
          {this.props.teamSettingsModalActive &&
          <TeamSettingsModal/>}
          {this.props.verifyTeamUserModal.active &&
          <VerifyTeamUserModal/>}
          {this.props.reactivateTeamUserModal.active &&
          <ReactivateTeamUserModal/>}
          {this.props.teamTransferOwnershipModal.active &&
          <TeamTransferOwnershipModal/>}
          {this.props.teamPhoneNumberModal.active &&
          <TeamPhoneNumberModal/>}
          {this.props.teamAddMultipleUsersModal.active &&
          <TeamAddMultipleUsersModal/>}
          {this.props.requestWebsiteModal.active &&
          <RequestWebsiteModal/>}
          {this.props.freeTrialEndModal.active &&
          <FreeTrialEndModal/>}
          {this.props.upgradeTeamPlanModal.active &&
          <UpgradeTeamPlanModal/>}
          {this.props.teamUserInviteLimitReachedModal.active &&
          <TeamUserInviteLimitReachedModal/>}
          {this.props.departureDateEndModal.active &&
          <DepartureDateEndModal/>}
          {this.props.catalogAddBookmarkModal.active &&
          <CatalogAddBookmarkModal/>}
          {this.props.catalogAddAppModal.active &&
          <ClassicAppModal/>}
          {this.props.catalogAddSSOAppModal.active &&
          <SsoAppModal/>}
          {this.props.modals.classicAppSettings.active &&
          <ClassicAppSettingsModal/>}
          {this.props.modals.extensionDownload.active &&
          <ExtensionDownloadModal/>}
          {this.props.modals.linkAppSettings.active &&
          <LinkAppSettingsModal/>}
          {this.props.modals.passwordLostInformation.active &&
          <PasswordLostInformationModal/>}
          {this.props.modals.lockedTeamApp.active &&
          <LockedTeamAppModal/>}
          {this.props.modals.logWithAppSettings.active &&
          <LogWithAppSettings/>}
          {this.props.modals.chooseAppCredentials.active &&
          <ChooseAppCredentialsModal/>}
          {this.props.modals.chooseAnyAppCredentials.active &&
          <ChooseAnyAppCredentialsModal/>}
          {this.props.modals.chooseSoftwareAppCredentials.active &&
          <ChooseSoftwareAppCredentialsModal/>}
          {this.props.modals.teamSingleAppSettings.active &&
          <TeamSingleAppSettingsModal/>}
          {this.props.modals.teamAnySingleAppSettings.active &&
          <TeamAnySingleAppSettingsModal/>}
          {this.props.modals.teamSoftwareSingleAppSettings.active &&
          <TeamSoftwareSingleAppSettingsModal/>}
          {this.props.modals.teamEnterpriseAppSettings.active &&
          <TeamEnterpriseAppSettingsModal/>}
          {this.props.modals.teamAnyEnterpriseAppSettings.active &&
          <TeamAnyEnterpriseAppSettingsModal/>}
          {this.props.modals.teamSoftwareEnterpriseAppSettings.active &&
          <TeamSoftwareEnterpriseAppSettingsModal/>}
          {this.props.modals.teamLinkAppSettings.active &&
          <TeamLinkAppSettingsModal/>}
          {this.props.modals.updateAppPassword.active &&
          <UpdateAppPasswordModal/>}
          {this.props.modals.ssoAppSettings.active &&
          <SsoAppSettingsModal/>}
          {this.props.modals.anyAppSettings.active &&
          <AnyAppSettingsModal/>}
          {this.props.modals.softwareAppSettings.active &&
          <SoftwareAppSettingsModal/>}
          {this.props.modals.newFeature.active &&
          <NewFeatureModal/>}
          {this.props.catalogAddAnyAppModal.active &&
          <AddAnyAppModal/>}
          {this.props.catalogAddSoftwareAppModal.active &&
          <AddSoftwareCredentialsModal/>}
          {this.props.inviteTeamUsersModal.active &&
          <InviteTeamUsersModal/>}
          {this.props.simpleAppFillerChooserModal.active &&
          <SimpleAppFillerChooserModal/>}
          {this.props.fillSimpleCardCredentialsModal.active &&
          <FillSimpleCardCredentialsModal/>}
          {this.props.modals.accountUpdate.active &&
          <AccountUpdateModal/>}
        </div>
    )
  }
};

export default ModalsContainer;